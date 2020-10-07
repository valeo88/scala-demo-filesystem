package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.files.Directory
import com.valeopopov.demofilesystem.filesystem.State

import scala.annotation.tailrec

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
    @tailrec
    def collapseRelativeTokens(tokens: List[String], result: List[String]): List[String] = {
      if (tokens.isEmpty) result
      else {
        tokens.head match {
          case "." => collapseRelativeTokens(tokens.tail, result)
          case ".." =>
            if (result.isEmpty) null
            else collapseRelativeTokens(tokens.tail, result.init)
          case _ => collapseRelativeTokens(tokens.tail, result :+ tokens.head)
        }
      }
    }

    val pathList = path.substring(1).split(Directory.SEPARATOR).toList.filter(!_.isEmpty)
    // need to eliminate dots in path
    val collapsedDots = collapseRelativeTokens(pathList, List())
    root.findDescendant(collapsedDots)
  }
}
