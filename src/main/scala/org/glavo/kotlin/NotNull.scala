package org.glavo.kotlin

/**
  * Created by Glavo on 17-8-3.
  *
  * @author Glavo
  * @since 0.1.0
  */
trait NotNull[+A]

object NotNull extends NotNull[Nothing] {
    @inline
    def apply[A <: AnyRef](a: A): NotNull[a.type] = {
        if(a == null)
            throw new NullPointerException
        NotNull
    }
}
