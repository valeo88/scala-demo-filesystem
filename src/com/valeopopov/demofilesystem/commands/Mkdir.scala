package com.valeopopov.demofilesystem.commands

import com.valeopopov.demofilesystem.files.{DirEntry, Directory}
import com.valeopopov.demofilesystem.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {
  override protected def doCreateNewEntry(state: State): DirEntry = Directory.empty(state.wd.path, name)
}
