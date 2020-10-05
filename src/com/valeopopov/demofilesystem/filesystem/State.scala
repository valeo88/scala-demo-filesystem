package com.valeopopov.demofilesystem.filesystem

import com.valeopopov.demofilesystem.files.Directory

class State(val root: Directory, val wd: Directory, val output: String) {

  def show(): Unit =
    print(State.SHELL_TOKEN)
    println(output)

  def setMessage(message: String): State = State(root, wd, message)
}

object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, wd: Directory, output: String = ""): State = new State(root, wd, output)
}
