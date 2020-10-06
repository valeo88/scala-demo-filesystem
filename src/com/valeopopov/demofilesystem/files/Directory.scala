package com.valeopopov.demofilesystem.files

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name) {
  // replace entry with entryName by newEntry in this directory
  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(!_.name.equals(entryName)) :+ newEntry)

  def findEntry(name: String): Option[DirEntry] = contents.find(_.name.equals(name))

  def addEntry(newEntry: DirEntry): Directory = new Directory(parentPath, name, contents :+ newEntry)

  def hasEntry(name: String): Boolean = findEntry(name).nonEmpty

  // transforms path to this dir: "/a/b/thisDir" -> ["a","b","thisDir"]
  def getAllDirsInPath: List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList.filter(!_.isEmpty)

  // find descendant directory by its path in this directory
  def findDescendant(path: List[String]): Option[Directory] =
    if (path.isEmpty) Option(this)
    else {
      findEntry(path.head).map(_.asDirectory).flatMap(_.findDescendant(path.tail)).orElse(Option.empty)
    }

  override def asDirectory: Directory = this
  override def getType: String = "Folder"
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("","")

  def empty(parentPath: String, name: String) = new Directory(parentPath, name, List.empty)
}