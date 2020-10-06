package com.valeopopov.demofilesystem.commands

import com.valeopopov.demofilesystem.files.{DirEntry, Directory, File}
import com.valeopopov.demofilesystem.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override protected def doCreateNewEntry(state: State): DirEntry = File.empty(state.wd.path, name)
}
