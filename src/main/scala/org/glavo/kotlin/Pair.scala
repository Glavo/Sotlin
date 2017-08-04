package org.glavo.kotlin

/**
  * Created by Glavo on 17-8-4.
  *
  * @author Glavo
  * @since 0.1.0
  */
class Pair[+A, +B](val self: (A, B)) extends AnyVal {
    def first: A = self._1

    def second: B = self._2

}

class _Pair[+T](val self: (T, T)) extends AnyVal {
    def toList: List[T] =
        self._1 :: self._2 :: Nil
}
