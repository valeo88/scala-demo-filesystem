package com.valeopopov.demofilesystem.commands

import com.valeopopov.demofilesystem.files.{DirEntry, Directory}
import com.valeopopov.demofilesystem.filesystem.State

class Mkdir(name: String) extends Command {

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
      doMkdir(state, name)
    }
  }

  // not support names like: .././dir1
  private def checkIllegal(name: String): Boolean = name.contains(".")

  private def doMkdir(state: State, name: String): State = {
    // recursive re-create structure of currentDirectory and add newEntry
    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).get.asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(currentDirectory, path.tail, newEntry))
      }
    }

    val wd = state.wd

    // if wd has path /a/b/ => ["a","b"]
    val allDirsInPath = wd.getAllDirsInPath
    // new dir in working directory
    val newDir = Directory.empty(wd.path, name)
    // update structure of directories from root
    val newRoot = updateStructure(state.root, allDirsInPath, newDir)
    // need to find working directory by its path
    // we can .get here because guarantee that newWd in path
    val newWd = newRoot.findDescendant(allDirsInPath).get

    // new state = new root and new wd, contains newDir
    State(newRoot, newWd)
  }


}
