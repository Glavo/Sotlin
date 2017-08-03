package org.glavo.sotlin

import scala.util.control.ControlThrowable

/**
  * Created by Glavo on 17-8-3.
  *
  * @author Glavo
  * @since 0.1.0
  */
object Return {
    @inline
    def apply[R](tag: Symbol)(value: R): Nothing =
        throw ReturnControl(tag, value)

    @inline
    implicit class ImplF0[R](val f: () => R) {
        @unchecked
        def tag(tag: Symbol): () => R = () => {
            try f() catch {
                case ReturnControl(`tag`, v: R) => v
            }
        }
    }

    @inline
    implicit class ImplF1[A, R](val f: (A) => R) {
        @unchecked
        def tag(tag: Symbol): (A) => R = (a) => {
            try f(a) catch {
                case ReturnControl(`tag`, v: R) => v
            }
            
        }
    }

}

case class ReturnControl[A](tag: Symbol, value: A) extends ControlThrowable
