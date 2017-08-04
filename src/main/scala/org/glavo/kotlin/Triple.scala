package org.glavo.kotlin

/**
  * Created by Glavo on 17-8-4.
  *
  * @author Glavo
  * @since 0.1.0
  */
class Triple[+A, +B, +C](val self: (A, B, C)) extends AnyVal {
    def first: A = self._1

    def second: B = self._2

    def third: C = self._3

}

class _Triple[+T](val self: (T, T, T)) extends AnyVal {
    def toList: List[T] =
        self._1 :: self._2 :: self._3 :: Nil
}
