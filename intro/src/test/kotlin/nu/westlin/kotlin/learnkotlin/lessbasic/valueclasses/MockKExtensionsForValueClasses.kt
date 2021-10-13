package nu.westlin.kotlin.learnkotlin.lessbasic.valueclasses

import io.mockk.ConstantMatcher
import io.mockk.MockKGateway.CallRecorder
import io.mockk.MockKMatcherScope
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

fun <T : Any> value(value: T): T =
    if (value::class.isValue || value::class.isInline) inlineValue(value)
    else value

@Suppress("UNCHECKED_CAST")
fun <T : Any> inlineValue(value: T): T {
    val valueName = value::class.primaryConstructor!!.parameters[0].name
    val valueProperty = value::class.declaredMemberProperties
        .find { it.name == valueName }!! as KProperty1<T, *>
    return valueProperty.get(value) as T
}

inline fun <reified T : Any> MockKMatcherScope.anyValue(): T =
    if (T::class.isValue || T::class.isInline) anyInlineValue()
    else any()


inline fun <reified T : Any> MockKMatcherScope.anyInlineValue(): T {
    val valueConstructor = T::class.primaryConstructor!!
    val valueType = valueConstructor.parameters[0].type.classifier as KClass<*>
    val callRecorder = getProperty("callRecorder") as CallRecorder
    val anyMatcher = callRecorder.matcher(ConstantMatcher<T>(true), valueType)
    return valueConstructor.call(anyMatcher)
}

val KClass<*>.isInline: Boolean
    get() = !isData &&
        primaryConstructor?.parameters?.size == 1 &&
        java.declaredMethods.any { it.name == "box-impl" }

fun <T : Any> returnValueClass(value: T): T {
    require(value::class.isValue)
    val constructor = value::class.primaryConstructor!!
    val constructorParameter = constructor.parameters[0]
    val memberProperty = value::class.declaredMemberProperties
        .first { it.name == constructorParameter.name }
        .apply { isAccessible = true }
        .let @Suppress("UNCHECKED_CAST") { it as KProperty1<T, T> }
    return memberProperty.get(value)
}

inline fun <reified T : Any> MockKMatcherScope.anyValueClass(): T {
    require(T::class.isValue)
    val constructor = T::class.primaryConstructor!!
    val constructorParameterType = constructor.parameters[0].type.classifier as KClass<*>
    val callRecorder = getProperty("callRecorder") as CallRecorder
    val anyMatcher = callRecorder.matcher(ConstantMatcher<T>(true), constructorParameterType)
    return constructor.call(anyMatcher)
}