package scalax.io

import java.nio.{ByteBuffer, CharBuffer}
import java.nio.channels.{ByteChannel, ReadableByteChannel, WritableByteChannel, Channels}
import java.io._
import java.lang.String

/**
 * Supporting classes for converting between resource types.
 */
protected[io] trait ResourceAdapter[+S] {
  def src:S
}

protected[io] object ResourceAdapter {
  def closeAction[A](src:CloseAction[A]):CloseAction[ResourceAdapter[A]]  = {
    src match {
      case CloseAction.Noop =>
        CloseAction.Noop
      case _ =>
        CloseAction((in:ResourceAdapter[A]) => src(in.src))
    }
  }
}
protected[io] class WritableChannelAdapter[+A <: OutputStream](opener: => A) extends WritableByteChannel with ResourceAdapter[A] {
  lazy val src = opener
  lazy val channel = Channels.newChannel(src)

  def close = channel.close()
  def isOpen = channel.isOpen()
  def write(p1: ByteBuffer) = channel.write(p1)
}
protected[io] class ReadableChannelAdapter[+A <: InputStream](opener: => A) extends ReadableByteChannel with ResourceAdapter[A] {
  lazy val src = opener
  lazy val channel = Channels.newChannel(src)

  def close = channel.close()
  def isOpen = channel.isOpen()

  def read(p1: ByteBuffer) = channel.read(p1)
}

protected[io] class ChannelReaderAdapter[+In <: ReadableByteChannel](opener: => In,codec:Codec) extends Reader with ResourceAdapter[In] {
  lazy val src = opener
  lazy val reader = Channels.newReader(src, codec.name)
  def read(p1: Array[Char], p2: Int, p3: Int) = reader.read(p1,p2,p3)
  override def reset = reader.reset
  override def mark(p1: Int) = reader.mark(p1)
  override def markSupported = reader.markSupported
  override def ready = reader.ready
  override def skip(p1: Long) = reader.skip(p1)
  override def read(p1: Array[Char]) = reader.read(p1)
  override def read = reader.read
  override def read(p1: CharBuffer) = reader.read(p1)

  def close = reader.close();
}
protected[io] class ChannelInputStreamAdapter[+In <: ReadableByteChannel](opener: => In) extends InputStream with ResourceAdapter[In] {
  lazy val src = opener
  lazy val stream = Channels.newInputStream(src)
  override def read(p1: Array[Byte]) = stream.read(p1)
  override def close = stream.close
  override def markSupported = stream.markSupported
  override def reset = stream.reset
  override def mark(p1: Int) = stream.mark(p1)
  override def available = stream.available
  override def skip(p1: Long) = stream.skip(p1)
  override def read(p1: Array[Byte], p2: Int, p3: Int) = stream.read(p1,p2,p3)
  override def read = stream.read
}
protected[io] class ChannelWriterAdapter[+In <: WritableByteChannel](opener: => In,codec:Codec) extends Writer with ResourceAdapter[In] {
  lazy val src = opener
  lazy val writer = Channels.newWriter(src,codec.name)

  def close = writer.close()
  def flush = writer.flush()
  def write(p1: Array[Char], p2: Int, p3: Int) = writer.write(p1,p2,p3)

  override def append(p1: Char) = writer.append(p1)
  override def append(p1: CharSequence, p2: Int, p3: Int) = writer.append(p1,p2,p3)
  override def append(p1: CharSequence) = writer.append(p1)
  override def write(p1: String, p2: Int, p3: Int) = writer.write(p1,p2,p3)
  override def write(p1: String) = writer.write(p1)
  override def write(p1: Array[Char]) = writer.write(p1)
  override def write(p1: Int) = writer.write(p1)
}
protected[io] class ChannelOutputStreamAdapter[+In <: WritableByteChannel](opener: => In) extends OutputStream with ResourceAdapter[In] {
  lazy val src = opener
  lazy val out = Channels.newOutputStream(src)

  def write(p1: Int) = out.write(p1)

  override def close = out.close()
  override def flush = out.flush()
  override def write(p1: Array[Byte], p2: Int, p3: Int) = out.write(p1,p2,p3)
  override def write(p1: Array[Byte]) = write(p1)
}