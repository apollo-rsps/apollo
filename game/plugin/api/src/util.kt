import java.lang.ref.WeakReference
import java.util.*
import kotlin.reflect.KProperty

val RAND = Random()

fun rand(bounds: Int): Int {
    return RAND.nextInt(bounds)
}

class WeakRefHolder<T>(private var _value: WeakReference<T?> = WeakReference(null)) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return _value.get()
    }

    operator fun setValue(
        thisRef: Any?, property: KProperty<*>, value: T?
    ) {
        _value = WeakReference(value)
    }
}
