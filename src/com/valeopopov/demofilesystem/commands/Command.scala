package com.valeopopov.demofilesystem.commands

import com.valeopopov.demofilesystem.filesystem.State

trait Command extends (State => State) {
}

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  def emptyCommand: Command =
    state => state

  def incompleteCommand(name: String): Command =
    state => state.setMessage(s"$name is incomplete")

  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")

    if (input.isEmpty || tokens.isEmpty) emptyCommand
    else tokens(0) match {
      case MKDIR =>
        if (tokens.length < 2) incompleteCommand(MKDIR)
        else new Mkdir(tokens(1))
      case TOUCH =>
        if (tokens.length < 2) incompleteCommand(TOUCH)
        else new Touch(tokens(1))
      case CD =>
        if (tokens.length < 2) incompleteCommand(CD)
        else new Cd(tokens(1))
      case LS => new Ls
      case PWD => new Pwd
      case RM =>
        if (tokens.length < 2) incompleteCommand(RM)
        else new Rm(tokens(1))
      case ECHO =>
        if (tokens.length < 2) incompleteCommand(ECHO)
        else new Echo(tokens.tail)
      case CAT =>
        if (tokens.length < 2) incompleteCommand(CAT)
        else new Cat(tokens(1))
      case _ => new UnknownCommand
    }
  }
}
