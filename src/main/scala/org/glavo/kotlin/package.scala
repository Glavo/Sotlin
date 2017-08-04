package org.glavo

import scala.language.implicitConversions
import scala.reflect.ClassTag

/**
  * Created by Glavo on 17-8-4.
  *
  * @author Glavo
  * @since 0.1.0
  */
package object kotlin {
    @inline
    def emptyArray[A: ClassTag]: Array[A] =
        Array[A]()

    @inline
    implicit def throwableWrapper(t: Throwable): Exceptions = new Exceptions(t)

    @inline
    implicit def nullable[A](nullable: A): Nullable[A] = new Nullable(nullable)

    /**
      * Creates a new instance of the [Lazy] that is already initialized with the specified [value].
      */
    def lazyOf[A](value: A): Lazy[A] = new InitializedLazyImpl(value)

    /**
      * Creates a new instance of the [Lazy] that uses the specified initialization function [initializer]
      * and the default thread-safety mode [LazyThreadSafetyMode.SYNCHRONIZED].
      *
      * If the initialization of a value throws an exception, it will attempt to reinitialize the value at next access.
      *
      * Note that the returned instance uses itself to synchronize on. Do not synchronize from external code on
      * the returned instance as it may cause accidental deadlock. Also this behavior can be changed in the future.
      */
    def `lazy`[A](initializer: () => A): Lazy[A] = new SynchronizedLazyImpl(initializer)

    /**
      * Creates a new instance of the [Lazy] that uses the specified initialization function [initializer]
      * and thread-safety [mode].
      *
      * If the initialization of a value throws an exception, it will attempt to reinitialize the value at next access.
      *
      * Note that when the [LazyThreadSafetyMode.SYNCHRONIZED] mode is specified the returned instance uses itself
      * to synchronize on. Do not synchronize from external code on the returned instance as it may cause accidental deadlock.
      * Also this behavior can be changed in the future.
      */
    def `lazy`[A](mode: LazyThreadSafetyMode, initializer: () => A): Lazy[A] =
        mode match {
            case LazyThreadSafetyMode.SYNCHRONIZED =>new SynchronizedLazyImpl(initializer)
            case LazyThreadSafetyMode.PUBLICATION => new SafePublicationLazyImpl(initializer)
            case LazyThreadSafetyMode.NONE =>new UnsafeLazyImpl(initializer)
        }

    implicit def pairWrapper[A, B](p: (A, B)): Pair[A, B] = new Pair(p)

    implicit def _pairWrapper[A](p: (A, A)): _Pair[A] = new _Pair(p)

    implicit def tripleWrapper[A, B, C](p: (A, B, C)): Triple[A, B, C] = new Triple(p)

    implicit def _tripleWrapper[A](p: (A, A, A)): _Triple[A] = new _Triple(p)
}
