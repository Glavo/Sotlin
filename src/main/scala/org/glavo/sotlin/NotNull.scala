package org.glavo.sotlin

/**
  * Created by Glavo on 17-8-3.
  *
  * @author Glavo
  * @since 0.1.0
  */
trait NotNull[+A]

object NotNull extends NotNull[Nothing] {
    @inline
    def apply[A <: AnyRef](a: A): a.type = {
        if(a == null)
            throw new NullPointerException
        a
    }
}
