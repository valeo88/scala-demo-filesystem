package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.files.Directory
import com.valeopopov.demofilesystem.filesystem.State

class Cd(dir: String) extends Command {

  override def apply(state: State): State = {
    val root = state.root
    val wd = state.wd

    val absolutePath =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) Directory.SEPARATOR + dir
      else wd.path + Directory.SEPARATOR + dir

    doFindEntry(root, absolutePath)
        .map(d => State(root, d))
        .getOrElse(state.setMessage(dir + ": no such directory"))
}

  def doFindEntry(root: Directory, path: String): Option[Directory] = {
    val pathList = path.substring(1).split(Directory.SEPARATOR).toList.filter(!_.isEmpty)
    root.findDescendant(pathList)
  }
}
