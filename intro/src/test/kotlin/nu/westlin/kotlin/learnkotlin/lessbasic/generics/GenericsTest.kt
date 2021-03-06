@file:Suppress("RemoveExplicitTypeArguments", "UNUSED_VARIABLE", "unused")

package nu.westlin.kotlin.learnkotlin.lessbasic.generics

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
            fun get(): T = value
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

    //@Test
    fun `bla bla`() {
        abstract class Base
        class Impl1 : Base()
        class Impl2 : Base()

        class BaseRegistry(val bases: List<Base>) {
            fun print() {
                for (base in bases) {
                    println("base = $base")
                }
            }

            fun getBase(base: Base) = bases.find { it::class == base::class }
            inline fun <reified T : Base> getBase() = bases.find { it::class is T } as T
        }

        val bases = listOf(Impl1(), Impl2())
        val registry = BaseRegistry(bases)
        registry.print()
        val a = registry.getBase(bases.first())
        val b = registry.getBase<Impl2>()
        println("registry.getBase(bases.first()) = ${registry.getBase(bases.first())}")
    }
}

interface Otypad
class Otypad1 : Otypad

interface OtypadAgain
class OtypadAgain1 : OtypadAgain

interface Typad<T : Typad<T>>
class Typad1 : Typad<Typad1>

interface Converter<O : Otypad, T : Typad<T>> {
    fun konvertera(utUtbytesobjekt: T): O
    fun konvertera(inUtbytesobjekt: O): T
}

interface Converter2<O : Otypad, T : OtypadAgain> {
    fun konvertera(utUtbytesobjekt: T): O
    fun konvertera(inUtbytesobjekt: O): T
}

class FooConverter : Converter<Otypad1, Typad1> {
    override fun konvertera(utUtbytesobjekt: Typad1): Otypad1 {
        TODO("not implemented")
    }

    override fun konvertera(inUtbytesobjekt: Otypad1): Typad1 {
        TODO("not implemented")
    }
}

class FooConverter2 : Converter2<Otypad1, OtypadAgain1> {
    override fun konvertera(inUtbytesobjekt: Otypad1): OtypadAgain1 {
        TODO("not implemented")
    }

    override fun konvertera(utUtbytesobjekt: OtypadAgain1): Otypad1 {
        TODO("not implemented")
    }
}

class GenericsAssHoleTest {

    //@Test
    @Suppress("UNCHECKED_CAST")
    fun `do it`() {
        val converter: Converter<*, *> = FooConverter()

        val converter21: Converter2<*, *> = FooConverter2()
        val converter22: Converter2<Otypad, OtypadAgain> = FooConverter2() as Converter2<Otypad, OtypadAgain>
        converter22.konvertera(Otypad1())
        converter22.konvertera(OtypadAgain1())
    }
}

/**
 * Inspired by https://blog.kotlin-academy.com/kotlin-generics-variance-modifiers-36b82c7caa39
 */
class GenericsInAndOutTest {

    open class B
    class A : B()


    /*
    Default is invariant.
    It means that there is no relation between any two types generated by this generic class.
    For instance, there is no relation between Cup<Int> and Cup<Number>, Cup<Any> or Cup<Nothing>.
     */
    class Cup<T>

    inner class DefaultOrInvariantTest {
        //val anys: Cup<Any> = Cup<Int>() // Error: Type mismatch
        //val nothings: Cup<Nothing> = Cup<Int>() // Error: Type mismatch
    }

    /*
    out makes type parameter covariant.
    It means that when A is subtype of B and Cup is covariant, then type Cup<A> is subtype of Cup<B>
    */
    class OutCup<out T>

    inner class OutOrCovariantTest {
        val b: OutCup<B> = OutCup<A>() // OK
        //val a: OutCup<A> = OutCup<B>() // Error: Type mismatch

        val anys: OutCup<Any> = OutCup<Int>() // OK
        //val nothings: OutCup<Nothing> = OutCup<Int>() // Error: Type mismatch

    }

    /*
    The opposite effect can be achieved using in modifier, which makes type parameter contravariant.
    It means that when A is subtype of B and Cup is contravariant, then type Cup<A> is supertype of Cup<B>
     */
    class InCup<in T>

    inner class InOrContravariantTest {
        //val b: InCup<B> = InCup<A>() // Error: Type mismatch
        val a: InCup<A> = InCup<B>() // OK

        //val anys: InCup<Any> = InCup<Int>() // Error: Type mismatch
        val nothings: InCup<Nothing> = InCup<Int>() // OK
    }


    /*
    Arrays in Kotlin have invariant type parameter.
    List interface has covariant type parameter, because it is immutable.
    MutableList has invariant type parameter.
     */

    /*
    Default variance behavior of type parameter is invariance.
    If Cup is invariant and A is subtype of B then there is no relation between Cup<A> and Cup<B>.

    out makes type parameter covariant.
    If Cup is covariant and A is subtype of B, then Cup<A> is subtype of Cup<B>.
    Covariant type can be used on out positions.

    in makes type parameter contravariant.
    If Cup is contravariant and A is subtype of B, then Cup<B> is subtype of Cup<A>.
    Contravariant type can be used on in positions.
     */
}

class MoreGenericsTest {
    open class Shape
    class Square: Shape()
    class Triangle: Shape()

    @Test
    fun `invariant type`() {
        class Item<T>

        val list = mutableListOf<Item<Square>>()
        list.add(Item<Square>())
        // list.add(Item<Shape>())      // Compile error
        // list.add(Item<Triangle>())   // Compile error

        val item: Item<Square> = list.first()
        // val item: Item<Shape> = list.first()     // Compile error
    }

    @Test
    fun `contravariant type`() {
        class Item<out T>

        val list = mutableListOf<Item<Shape>>()
        list.add(Item<Square>())
        list.add(Item<Shape>())
        list.add(Item<Triangle>())

        //val item: Item<Square> = list.first()   // Compile error
        val item: Item<Shape> = list.first()
    }

    @Test
    fun `covariant type`() {
        class Item<in T>

        val list = mutableListOf<Item<Shape>>()
        // list.add(Item<Square>())    // Compile error
        list.add(Item<Shape>())
        // list.add(Item<Triangle>())  // Compile error

        val itemSquare: Item<Square> = list.first()
        val itemShape: Item<Shape> = list.first()
    }
}