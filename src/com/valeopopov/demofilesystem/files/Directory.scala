package com.valeopopov.demofilesystem.files

import com.valeopopov.demofilesystem.filesystem.FilesystemException

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {
  def isRoot: Boolean = parentPath.isEmpty || parentPath.equals(Directory.ROOT_PATH)

  // replace entry with entryName by newEntry in this directory
  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(!_.name.equals(entryName)) :+ newEntry)

  def findEntry(entryName: String): Option[DirEntry] = contents.find(_.name.equals(entryName))

  def addEntry(newEntry: DirEntry): Directory = new Directory(parentPath, name, contents :+ newEntry)

  def removeEntry(entryName: String) =
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(!_.name.equals(entryName)))

  def hasEntry(name: String): Boolean = findEntry(name).nonEmpty

  // transforms path to this dir: "/a/b/thisDir" -> ["a","b","thisDir"]
  def getAllDirsInPath: List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList.filter(!_.isEmpty)

  // find descendant directory by its path in this directory
  def findDescendant(path: List[String]): Option[Directory] =
    if (path == null) Option.empty
    else if (path.isEmpty) Option(this)
    else {
      findEntry(path.head)
        .filter(_.isDirectory)
        .flatMap(_.asDirectory.findDescendant(path.tail))
        .orElse(Option.empty)
    }

  def findDescendant(path: String): Option[Directory] =
    findDescendant(path.substring(1).split(Directory.SEPARATOR).toList.filter(!_.isEmpty))

  override def asDirectory: Directory = this
  override def asFile: File = throw new FilesystemException("Directory can't be a file")
  override def getType: String = "Folder"
  override def isDirectory: Boolean = true
  override def isFile: Boolean = false
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty(ROOT_PATH,"")

  def empty(parentPath: String, name: String) = new Directory(parentPath, name, List.empty)
}