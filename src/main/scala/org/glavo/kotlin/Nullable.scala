package org.glavo.kotlin

/**
  * Created by Glavo on 17-8-4.
  *
  * @author Glavo
  * @since 0.1.0
  */
class Nullable[A](val self: A) extends AnyVal {
    @inline
    def !! : A =
        if(self == null) throw new NullPointerException
        else self

    @inline
    def ?[B <: AnyRef](f: A => B): B =
        if(self != null && f != null) f(self)
        else null.asInstanceOf

    def :?[B >: A](b: B) : B =
        if(self == null) b
        else self
}
