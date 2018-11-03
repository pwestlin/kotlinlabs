@file:Suppress("RemoveExplicitTypeArguments", "UNUSED_VARIABLE", "unused")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.generics

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class XmasGift<T>(t: T) {
    var value = t
}

class Consumer<in T> {
    fun convertToInt(value: T) = when (value) {
        is Int -> value
        is String -> Integer.parseInt(value)
        else -> throw RuntimeException("Value $value can't be converted to an Int")
    }

    //fun returnSomeThingWithTypeT() : T {}     // Funakr inte för T är satt till in
}

class Producer<out T>(private val list: List<T>) {
    fun first() = list.first()
    //fun add(value: T) {list.}     // Funkar inte för T är satt till out
}

class GenericsTest {

    @Test
    fun `create Box`() {
        XmasGift<Int>(3)    // Int
        XmasGift(3)         // Infer type as Int
        XmasGift("Foo")     // Infer type as String
    }

    @Test
    fun `consume some stuff`() {
        val stringConsumer = Consumer<String>()
        assertThat(stringConsumer.convertToInt("123")).isEqualTo(123)

        val intConsumer = Consumer<Int>()
        assertThat(intConsumer.convertToInt(123)).isEqualTo(123)
    }

    @Test
    fun `produce some stuff`() {
        val intProducer = Producer(listOf(7, 9, 42))
        assertThat(intProducer.first()).isEqualTo(7)

        val stringProducer = Producer(listOf("foo", "bar"))
        assertThat(stringProducer.first()).isEqualTo("foo")
    }

    @Test
    fun `having !fun with array`() {
        fun copy(from: Array<Any>, to: Array<Any>) {
            assert(from.size == to.size)
            for (i in from.indices)
                to[i] = from[i]
        }

        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array<Any>(3) { "" }
        // copy(ints, any)     //   ^ type is Array<Int> but Array<Any> was expected
    }

    @Test
    fun `having fun with array`() {
        fun copy(from: Array<out Any>, to: Array<Any>) {
            assert(from.size == to.size)
            for (i in from.indices)
                to[i] = from[i]
        }

        val ints: Array<Int> = arrayOf(1, 2, 3)
        val any = Array<Any>(3) { "" }
        copy(ints, any)

        assertThat(any).containsExactly(1, 2, 3)    // Nu går det fint
    }

    @Test
    fun `going crazy`() {
        class Bean<T>(private var value: T) {
            fun get() : T = value
            fun set(value: T) {
                this.value = value
            }
        }

        fun getValue(from: Bean<Any>, to: Bean<Any>) {
            to.set(from.get())
        }

        val bean1 = Bean(1)
        val bean2 = Bean("1")

        //copy(bean1, bean2)        // Går inte för kompilatorn vill ha Bean<Any>
    }


}