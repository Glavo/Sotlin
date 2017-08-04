package org.glavo.kotlin


/**
  * Created by Glavo on 17-8-4.
  *
  * @author Glavo
  * @since 0.1.0
  */
trait Lazy[+A] {
    def value: A

    def isInitialized: Boolean
}

sealed abstract class LazyThreadSafetyMode

/**
  * Specifies how a [Lazy] instance synchronizes access among multiple threads.
  */
object LazyThreadSafetyMode {

    /**
      * Locks are used to ensure that only a single thread can initialize the [Lazy] instance.
      */
    case object SYNCHRONIZED extends LazyThreadSafetyMode

    /**
      * Initializer function can be called several times on concurrent access to uninitialized [Lazy] instance value,
      * but only first returned value will be used as the value of [Lazy] instance.
      */
    case object PUBLICATION extends LazyThreadSafetyMode

    /**
      * No locks are used to synchronize the access to the [Lazy] instance value; if the instance is accessed from multiple threads, its behavior is undefined.
      *
      * This mode should be used only when high performance is crucial and the [Lazy] instance is guaranteed never to be initialized from more than one thread.
      */
    case object NONE extends LazyThreadSafetyMode

}

private[kotlin] object UNINITIALIZED_VALUE

private[kotlin] class SynchronizedLazyImpl[T](private var initializer: () => T,
                                              _lock: AnyRef = null)
    extends Lazy[T] with Serializable {
    @volatile private var _value: AnyRef = UNINITIALIZED_VALUE
    // final field is required to enable safe publication of constructed instance
    private val lock = if (_lock != null) _lock else this

    override def value: T = {
        val _v1 = _value
        if (_v1.asInstanceOf[AnyRef] ne UNINITIALIZED_VALUE) {
            _v1.asInstanceOf[T]
        } else lock.synchronized {
            val _v2 = _value
            if (_v2.asInstanceOf[AnyRef] ne UNINITIALIZED_VALUE) {
                _v2.asInstanceOf
            } else {
                val typedValue = initializer()
                _value = typedValue.asInstanceOf[AnyRef]
                initializer = null
                typedValue
            }
        }
    }

    override def isInitialized(): Boolean =
        _value ne UNINITIALIZED_VALUE

    override def toString: String = if (isInitialized()) value.toString else "Lazy value not initialized yet."

    private def writeReplace(): Any = new InitializedLazyImpl(value)
}

// internal to be called from lazy in JS
private[kotlin] class UnsafeLazyImpl[T](private var initializer: () => T)
    extends Lazy[T] with Serializable {

    private var _value: AnyRef = UNINITIALIZED_VALUE

    override def value: T = {
        if (_value eq UNINITIALIZED_VALUE) {
            _value = initializer().asInstanceOf
            initializer = null
        }
        _value.asInstanceOf[T]
    }

    override def isInitialized: Boolean =
        _value ne UNINITIALIZED_VALUE

    override def toString: String =
        if (isInitialized) value.toString
        else "Lazy value not initialized yet."

    private def writeReplace(): Any =
        new InitializedLazyImpl(value)
}

private[kotlin] class InitializedLazyImpl[T](override val value: T) extends Lazy[T] with Serializable {

    override def isInitialized: Boolean = true

    override def toString: String = value.toString

}

private[kotlin] class SafePublicationLazyImpl[T](private var initializer: () => T)
    extends Lazy[T] with Serializable {
    @volatile private var _value: AnyRef = UNINITIALIZED_VALUE

    // this final field is required to enable safe publication of constructed instance
    private val `final`: Any = UNINITIALIZED_VALUE

    override def value: T = {
        if (_value eq UNINITIALIZED_VALUE) {
            val initializerValue = initializer
            // if we see null in initializer here, it means that the value is already set by another thread
            if (initializerValue != null) {
                val newValue = initializerValue()
                if (SafePublicationLazyImpl.valueUpdater.compareAndSet(this, UNINITIALIZED_VALUE, newValue)) {
                    initializer = null
                }
            }
        }
        _value.asInstanceOf
    }

    override def isInitialized: Boolean = _value ne UNINITIALIZED_VALUE

    override def toString: String = if (isInitialized) value.toString else "Lazy value not initialized yet."

    private def writeReplace(): Any = new InitializedLazyImpl(value)
}

object SafePublicationLazyImpl {
    private val valueUpdater =
        java.util.concurrent.atomic.AtomicReferenceFieldUpdater.newUpdater(
            classOf[SafePublicationLazyImpl[_]],
            classOf[Any],
            "_value"
        )
}