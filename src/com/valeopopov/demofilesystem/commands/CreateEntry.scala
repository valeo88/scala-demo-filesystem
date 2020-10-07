package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.files.{DirEntry, Directory}
import com.valeopopov.demofilesystem.filesystem.State

abstract class CreateEntry(name: String) extends Command {
  override def apply(state: State): State = {
    val wd = state.wd

    // checks for not supported cases
    // support only case like: mkdir <dirname>
    if (wd.hasEntry(name)) {
      state.setMessage(s"Entry $name already exists")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage("Name must not contains separators")
    } else if (checkIllegal(name)) {
      state.setMessage(s"$name is illegal")
    } else {
      doCreate(state, name)
    }
  }

  // not support names like: .././dir1
  private def checkIllegal(name: String): Boolean = name.contains(".")

  private def doCreate(state: State, name: String): State = {
    // recursive re-create structure of currentDirectory and add newEntry
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        currentDirectory.findEntry(path.head)
          .map(_.asDirectory)
          .map(oldEntry => currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry)))
          .get
      }
    }

    val wd = state.wd

    // if wd has path /a/b/ => ["a","b"]
    val allDirsInPath = wd.getAllDirsInPath
    // new entry in working directory
    val newEntry = doCreateNewEntry(state)
    // update structure of directories from root
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)
    // need to find working directory by its path
    // we can .get here because guarantee that newWd in path
    val newWd = newRoot.findDescendant(allDirsInPath).get

    // new state = new root and new wd, contains newDir
    State(newRoot, newWd)
  }

  protected def doCreateNewEntry(state: State): DirEntry
}
