import org.apollo.game.model.entity.Mob
import org.apollo.game.model.entity.attr.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object MobAttributeDelegators {
    inline fun <reified T : Any> attribute(name: String, defaultValue: T): MobAttributeDelegate<T> {
        return MobAttributeDelegate<T>(name, T::class, defaultValue, false)
    }

    inline fun <reified T : Any> persistentAttribute(name: String, defaultValue: T): MobAttributeDelegate<T> {
        return MobAttributeDelegate<T>(name, T::class, defaultValue, true)
    }

}

class MobAttributeDelegate<T : Any>(val name: String, val type: KClass<T>, val defaultValue: T, persistent: Boolean = true) {
    init {
        val attrType = when {
            type == Int::class || type == Long::class -> AttributeType.LONG
            type == Boolean::class -> AttributeType.BOOLEAN
            type == Double::class -> AttributeType.DOUBLE
            type == String::class -> AttributeType.STRING
            else -> throw IllegalArgumentException("Can only store primitive attributes, not: ${type.qualifiedName}")
        }

        val definition = AttributeDefinition(defaultValue, if (persistent) AttributePersistence.PERSISTENT else AttributePersistence.TRANSIENT, attrType)
        AttributeMap.define(name, definition)
    }

    operator fun getValue(thisRef: Mob, property: KProperty<*>): T {
        return thisRef.attributes[name]?.value as T? ?: defaultValue
    }

    operator fun setValue(thisRef: Mob, property: KProperty<*>, value: T) {
        val attr = when {
            type == Double::class || type == Int::class || type == Long::class -> NumericalAttribute(value as Number)
            type == Boolean::class -> BooleanAttribute(value as Boolean)
            type == String::class -> StringAttribute(value as String, false)
            else -> throw IllegalArgumentException("Can only store primitive attributes, not: ${type.qualifiedName}")
        }

        thisRef.setAttribute(name, attr)
    }
}
