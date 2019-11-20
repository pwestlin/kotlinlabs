@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.Instant

class LambdasTest {

    class LambdasStandardLibrary {
        private val list = listOf("Hello", "from", "the", "wonderful", "world", "of", "Kotlin", "programming")

        @Test
        fun `last`() {
            assertThat(list.last()).isEqualTo("programming")

            // it?
            assertThat(list.last({ it.length == 6 })).isEqualTo("Kotlin")
            // samma som ovan men it är namnsatt till string
            assertThat(list.last({ string -> string.length == 6 })).isEqualTo("Kotlin")

            // Om den sista parametern til en funktion är ett lambda
            // kan (och ska enligt god sed) den flyttas ut ur paranteserna
            assertThat(list.last { it.length == 6 }).isEqualTo("Kotlin")
        }

        @Test
        fun `findLast`() {
            assertThat(list.findLast { it.length == 6 }).isEqualTo("Kotlin")
        }

        // filter, map, first, groupBy, let, apply osv osv osv osv osv....
    }

    @Test
    fun `lambda compute`() {
        val add = { a: Int, b: Int -> a + b }
        val subtract = { a: Int, b: Int -> a - b }
        val multiply = { a: Int, b: Int -> a * b }

        fun compute(a: Int, b: Int, function: (Int, Int) -> Int): Int {
            return function(a, b)
        }

        assertThat(compute(4, 2, add)).isEqualTo(6)
        assertThat(compute(4, 2, subtract)).isEqualTo(2)
        assertThat(compute(4, 2, multiply)).isEqualTo(8)
        assertThat(compute(4, 2, { a: Int, b: Int -> a / b })).isEqualTo(2)
        assertThat(compute(4, 2) { a: Int, b: Int -> a / b }).isEqualTo(2)
    }


    @Suppress("DIVISION_BY_ZERO")
    @Test
    fun `when does a lambda run?`() {
        fun divisionByZero(): Int {
            println("${Instant.now()}: Doing calculation")
            return 1 / 0
        }

        println("${Instant.now()}: Before runValue")
        fun runValue(int: Int) = int

        assertThatThrownBy { runValue(divisionByZero()) }
            .isInstanceOf(ArithmeticException::class.java)

        fun <R> runLambda(function: () -> R) = function()
        println("${Instant.now()}: Before runValue")
        assertThatThrownBy { runLambda({ divisionByZero() }) }
            .isInstanceOf(ArithmeticException::class.java)

    }

    @Suppress("SimplifyBooleanWithConstants")
    @Test
    fun `do something`() {
        fun doSomethingIfSomething(someCondition: () -> Boolean, doSomething: () -> Unit) {
            if (someCondition()) doSomething()
        }

        doSomethingIfSomething({ 4 / 2 == 1 }, { println("Doing something!") })
        doSomethingIfSomething({ 4 / 2 == 2 }, { println("Doing something!") })
    }

    @Suppress("SimplifyBooleanWithConstants")
    @Test
    fun `do something if else`() {
        fun <T> doSomethingIfSomethingOrElseSomethingElse(someCondition: () -> Boolean, doSomething: () -> T, doSomethingElse: () -> T): T {
            return if (someCondition()) doSomething() else doSomethingElse()
        }

        println(doSomethingIfSomethingOrElseSomethingElse({ 4 / 2 == 1 }, { "Correct!" }, { "Uncorrect" }))
        println(doSomethingIfSomethingOrElseSomethingElse({ 4 / 2 == 2 }, { "Correct!" }, { "Uncorrect" }))
    }

    @Test
    fun `lambda extension`() {
        class Status(var code: Int, var description: String)

        fun status(status: Status.() -> Unit) {}
        // This will allow the following statement
        status {
            code = 404
            description = "Not found"
        }
    }

    @Test
    fun `misc lambdas`() {
        val plus = { a: Int, b: Int -> a + b }
        assertThat(plus(2, 5)).isEqualTo(7)

        val square = { int: Int -> int * int }
        assertThat(square(9)).isEqualTo(81)
        // If we specify type we can refer to the (only) param as it
        val square2: (Int) -> Int = { it * it }
        assertThat(square2(9)).isEqualTo(81)

        // Param: lambda with Int-param that results in an Int, return type: Int
        val apply5: ((Int) -> Int) -> Int = { it(5) }
        assertThat(apply5 { it + it }).isEqualTo(10)
        assertThat(apply5 { square(it) }).isEqualTo(25)
        assertThat(apply5 { square2(it) }).isEqualTo(25)

        // Param: Int, return type: lambda with Int-param that result in an Int
        val applySum: (Int) -> ((Int) -> Int) =
            { x -> { it + x } }
        assertThat(applySum(5)(9)).isEqualTo(14)

        // Param: Lambda with param Int and result type Int, Result: Lambda with param Int and result type Int
        val applyInverse: ((Int) -> Int) -> ((Int) -> Int) =
            { f -> { -1 * f(it) } }
        assertThat(applyInverse { it + 5 }(5)).isEqualTo(-10)
    }

    // A lambda with a receiver allows you to call methods of an object in the body of a lambda without any qualifiers.

    @Test
    fun `lambda with a receiver`() {
        fun <T, R> doWith(receiver: T, block: T.() -> R): R {
            return receiver.block()
        }

        val string = "foo"
        val newString = doWith(string) {
            plus("bar")
        }

        assertThat(newString).isEqualTo("foobar")

        val stringBuilder = StringBuilder("foo")
        doWith(stringBuilder) {
            append("bar")
            append("rab")
            append("oof")
        }

        assertThat(stringBuilder.toString()).isEqualTo("foobarraboof")

        // Actually, doWith above is exactly the same impl. as Kotlins built-in with :)
    }

    @Test
    // https://proandroiddev.com/kotlin-pearls-lambdas-with-a-context-58f26ab2eb1d
    fun `function literals with a receiver`() {
        fun square(x: Int): Int = x * x
        fun squared(a: Int, f: (Int) -> Int): Int = f(square(a))
        fun squared2(a: Int, f: Int.() -> Int): Int = f(square(a))

        assertThat(square(3)).isEqualTo(9)
        assertThat(squared(3, { it + 3 })).isEqualTo(12)
        assertThat(squared2(3, { this + 3 })).isEqualTo(12)


        class Car {
            fun forward() {}
            fun backward() {}
            fun left() {}
            fun right() {}
        }

        class CarService {
            fun drive(block: Car.() -> Unit) {
                val car = Car()
                block(car)
            }
        }

        CarService().drive {
            left()
            forward()
            right()
            forward()
        }
    }

    @Test
    fun `some DSL fun with receivers`() {
        class Engine {
            var hp: Int? = null
        }

        class Car {
            var engine: Engine? = null
            var noSeats: Int? = null

            fun engine(block: Engine.() -> Unit) {
                engine = Engine().apply(block)
            }
        }

        fun car(block: Car.() -> Unit): Car {
            return Car().apply(block)
        }

        val car = car {
            engine {
                hp = 204
            }
            noSeats = 5
        }

        with(car) {
            assertThat(engine?.hp).isEqualTo(204)
            assertThat(noSeats).isEqualTo(5)
        }

        @Test
        fun `lambdas are "lazy"`() {
            class Logger(private val level: String) {
                fun log(level: String, message: String) {
                    if (level == this.level) println(message)
                }

                fun log(level: String, f: () -> String) {
                    if (level == this.level) println(f())
                }
            }

            class Foo {
                fun bar(string: String) = string
            }

            val foo = mockk<Foo>(relaxed = true)

            val logger = Logger("INFO")
            logger.log("DEBUG", foo.bar("another string"))
            logger.log("DEBUG") { foo.bar("a string") }

            verify(exactly = 0) { foo.bar("a string") }
            verify(exactly = 1) { foo.bar("another string") }
        }
    }
}

internal class FunctionReferenceAndCompositionTest {

    private data class Book(val name: String = "The Book", val price: Double = 22.55, val weight: Double = 0.68)

    private val book1 = Book("Another book", 1.11, 1.63)
    private val book2 = Book("Another book", 2.22, 2.63)
    private val book3 = Book("Another book", 3.33, 3.63)
    private val book4 = Book("Another book", 11.11, 4.63)
    private val book5 = Book("Another book", 22.22, 5.63)

    private val books = listOf(book1, book2, book3, book4, book5)

    private fun weight(book: Book) = book.weight
    private fun price(book: Book) = book.price
    private fun heavy(book: Book) = book.weight > 3
    private fun cheap(book: Book) = book.price < 10

    @Test
    fun `function reference`() {
        val weightFun = ::weight
        assertThat(weightFun(Book())).isEqualTo(0.68)

        val priceFun = ::price
        assertThat(priceFun(Book())).isEqualTo(22.55)

        assertThat(books.filter(::heavy)).containsExactlyInAnyOrder(book3, book4, book5)

        assertThat(books.filter(::cheap)).containsExactlyInAnyOrder(book1, book2, book3)
    }

    @Test
    fun `function composition`() {
        fun isOdd(x: Int) = x % 2 != 0
        fun length(s: String) = s.length

        fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
            // first execute function g with param x and then execute function f with the result of g(x) as param
            return { x -> f(g(x)) }
        }

        val oddLength = compose(::isOdd, ::length)
        val strings = listOf("a", "ab", "abc")
        println(strings.filter(oddLength))
    }
}
