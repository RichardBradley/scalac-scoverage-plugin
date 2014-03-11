package scoverage

import java.io._
import java.nio.charset.Charset

/** @author Stephen Samuel */
object Invoker {

  /**
   * We record that the given id has been invoked by appending its id to the coverage
   * data file.
   * This will happen concurrently on as many threads as the application is using,
   * but appending to a file is atomic on both POSIX and Windows if it is a single
   * write of a small enough string.
   *
   * @see http://stackoverflow.com/questions/1154446/is-file-append-atomic-in-unix
   * @see http://stackoverflow.com/questions/3032482/is-appending-to-a-file-atomic-with-windows-ntfs
   */
  def invoked(id: Int, path: String) = {
    val writer = new FileOutputStream(path, true)
    // We need to use write(byte[]) here, as all the write(string) methods on the
    // various Writer implementations in Java do not result in a single underlying
    // write.
    // Any charset is OK, as all chars here are ASCII
    val bytes = (id.toString + ';').getBytes(Charset.defaultCharset())
    writer.write(bytes)
    writer.close()
  }
}
