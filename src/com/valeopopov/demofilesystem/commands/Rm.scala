package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.files.Directory
import com.valeopopov.demofilesystem.filesystem.State

class Rm(name: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd

    val absolutePath =
      if (name.startsWith(Directory.SEPARATOR)) name
      else if (wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name

    if (Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("OOOO, NO")
    else
      doRm(state, absolutePath)

  }

  private def doRm(state: State, path: String): State = {
    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        //val nextEntry = currentDirectory.findEntry(path.head)
        currentDirectory.findEntry(path.head)
          .filter(_.isDirectory)
            .map(_.asDirectory)
            .map(directory => {
              val nextNewDirectory = rmHelper(directory, path.tail)
              if (nextNewDirectory == directory) currentDirectory
              else currentDirectory.replaceEntry(path.head, nextNewDirectory)
            })
          .getOrElse(currentDirectory)
      }
    }

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root)
      state.setMessage(path + ": not such file or directory")
    else
      newRoot.findDescendant(state.wd.path)
          .map(dir => State(newRoot, dir))
          .getOrElse(state.setMessage(path + ": error on recreate state"))
  }
}
