package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.files.DirEntry
import com.valeopopov.demofilesystem.filesystem.State

class Ls extends Command {
  override def apply(state: State): State = {
    val contents = state.wd.contents
    state.setMessage(niceOuput(contents))
  }

  private def niceOuput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + "[" + entry.getType + "]" + "\n" + niceOuput(contents.tail)
    }
  }
}
