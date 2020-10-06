package com.valeopopov.demofilesystem.files

import com.valeopopov.demofilesystem.filesystem.FilesystemException

class File(override val parentPath: String, override val name: String, content: String)
  extends DirEntry(parentPath, name) {

  override def asDirectory: Directory = throw new FilesystemException("File can't be converted to directory")

  override def asFile: File = this

  override def getType: String = "File"
}

object File {
  def empty(parentPath: String, name: String) = new File(parentPath, name, "")
}