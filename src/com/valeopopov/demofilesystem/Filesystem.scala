package com.valeopopov.demofilesystem

import java.util.Scanner

import com.valeopopov.demofilesystem.commands.Command
import com.valeopopov.demofilesystem.files.Directory
import com.valeopopov.demofilesystem.filesystem.State

object Filesystem extends App {

  val root = Directory.ROOT
  val scanner = new Scanner(System.in)

  var state = State(root, root)

  while (true) {
    state.show()
    state = Command.from(scanner.nextLine()).apply(state)
  }

}
