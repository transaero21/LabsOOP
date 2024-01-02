package ru.transaero21.mt

import kotlin.math.ceil
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

internal const val DELTA_TIME = 1F / 30

internal fun getRealFloatSum(float: Float, count: Int): Float {
    var result = 0F
    repeat(count) { result += float }
    return result
}

internal fun getRealFrames(deltaTime: Float, frames: Int): Int {
    val time = getRealFloatSum(float = deltaTime, count = frames)
    return ceil(x = time / deltaTime).toInt()
}

internal fun Any.callPrivateFunc(name: String, vararg args: Any?): Any? {
    return this::class.declaredMemberFunctions.firstOrNull { it.name == name }?.let { func ->
        func.isAccessible = true
        func.call(this, *args)
    }
}


internal fun Any.setFieldValue(fieldName: String, value: Any) {
    this::class.memberProperties.firstOrNull { it.name == fieldName }?.let { prop ->
        if (prop is KMutableProperty<*>) {
            prop.isAccessible = true
            prop.setter.call(this, value)
        }
    }
}

@Suppress(names = ["UNCHECKED_CAST"])
internal fun <T> Any.getFieldValue(fieldName: String): T? {
    return this::class.memberProperties.firstOrNull { it.name == fieldName }?.let { prop ->
        prop.isAccessible = true
        prop.getter.call(this) as T
    }
}
