package com.valeopopov.demofilesystem.commands

import com.valeopopov.demofilesystem.filesystem.State

trait Command {
  def apply(state: State): State
}

object Command {
  def from(input: String): Command = new UnknownCommand
}
