package org.glavo.kotlin

import java.io.{OutputStream, PrintStream, PrintWriter, Writer}


/**
  * Created by Glavo on 17-8-4.
  *
  * @author Glavo
  * @since 0.1.0
  */
class Exceptions(val self: Throwable) extends AnyVal {
    @inline
    def stackTrace: Array[StackTraceElement] =
        self.getStackTrace

    @inline
    def suppressed: Array[Throwable] =
        self.getSuppressed

    @inline
    def printStackTrace(stream: OutputStream): Unit =
        self.printStackTrace(new PrintStream(stream))

    @inline
    def printStackTrace(stream: Writer): Unit =
        self.printStackTrace(new PrintWriter(stream))
}
