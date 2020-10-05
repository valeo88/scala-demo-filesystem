package com.valeopopov.demofilesystem.commands
import com.valeopopov.demofilesystem.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State = state.setMessage("Command is not found")
}
