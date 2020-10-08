package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.filesystem.State

class Cat(name: String) extends Command {
  override def apply(state: State): State = {
    state.wd.findEntry(name)
      .filter(_.isFile)
      .map(_.asFile)
      .map(file => State(state.root, state.wd, file.content))
      .getOrElse(state.setMessage(name + ": file not found"))
  }
}
