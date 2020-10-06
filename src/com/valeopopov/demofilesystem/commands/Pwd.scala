package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
