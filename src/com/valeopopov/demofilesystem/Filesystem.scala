package com.valeopopov.demofilesystem


import com.valeopopov.demofilesystem.commands.Command
import com.valeopopov.demofilesystem.files.Directory
import com.valeopopov.demofilesystem.filesystem.State

object Filesystem extends App {

  val root = Directory.ROOT
  val initState = State(root, root)

  initState.show()
  io.Source.stdin.getLines().foldLeft(initState)((currentState, newLine) => {
    val newState = Command.from(newLine).apply(currentState)
    newState.show()
    newState
  })

}
