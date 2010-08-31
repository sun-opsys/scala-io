/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2009-2010, Jesse Eichar             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scalax.io.defaultfs

import scalax.io.{
  FileSystem, Path, FileOps,Codec,PathMatcher,DirectoryStream
}
import java.io.{File=>JFile}

/**
 * @author  Jesse Eichar
 * @since   1.0
 */
private[io] class DefaultFileSystem extends FileSystem {
  def separator: String = JFile.separator
  def apply(path: String): DefaultPath = apply (new JFile (path))
  def apply(path: JFile): DefaultPath = new DefaultPath (path, this)
  def roots = JFile.listRoots().toList map {f=> apply (f.getPath)}
  def createTempFile(prefix: String = randomPrefix, 
                   suffix: String = null, 
                   dir: String = null,
                   deleteOnExit : Boolean = true
                   /*attributes:List[FileAttributes] TODO */ ) : Path = {
    val dirFile = if(dir==null) null else new JFile(dir)
    val path = apply(JFile.createTempFile(prefix, suffix, dirFile).getPath)
    if(deleteOnExit) path.jfile.deleteOnExit
    path
  }

  def createTempDirectory(prefix: String = randomPrefix,
                        suffix: String = null, 
                        dir: String = null,
                        deleteOnExit : Boolean = true
                        /*attributes:List[FileAttributes] TODO */) : Path = {
    val path = createTempFile(prefix, suffix, dir, false)
    path.delete(force=true)
    path.createDirectory()
    if(deleteOnExit) {
      Runtime.getRuntime.addShutdownHook(new Thread{override def run:Unit = path.deleteRecursively(true) })
    }
    path
  }
  def matcher(pattern:String, syntax:String = PathMatcher.StandardSyntax.GLOB): PathMatcher = null // TODO

  override def toString = "Default File System"
}