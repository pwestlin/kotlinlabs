@file:Suppress(
    "UNUSED_VARIABLE",
    "UNUSED_VALUE",
    "RedundantExplicitType",
    "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE",
    "VARIABLE_WITH_REDUNDANT_INITIALIZER",
    "ALWAYS_NULL",
    "UNNECESSARY_SAFE_CALL",
    "EXPERIMENTAL_FEATURE_WARNING",
    "MemberVisibilityCanBePrivate",
    "SimplifyBooleanWithConstants",
    "ConstantConditionIf",
    "MoveLambdaOutsideParentheses",
    "UnnecessaryVariable",
    "unused",
    "UNUSED_PARAMETER",
    "RemoveRedundantBackticks",
    "NullChecksToSafeCall",
    "LiftReturnOrAssignment",
    "ReplaceGetOrSet",
    "NonAsciiCharacters",
    "PackageName",
    "ClassName"
)

package nu.westlin.kotlin.learnkotlin.basics

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import nu.westlin.kotlin.learnkotlin.basics.PantDSLTest.Hindertyp.ALLVARLIGT_FEL
import nu.westlin.kotlin.learnkotlin.basics.PantDSLTest.Hindertyp.KFM_SPARR
import nu.westlin.kotlin.learnkotlin.basics.PantDSLTest.TransactionManager.Work
import nu.westlin.kotlin.learnkotlin.lessbasic.coroutines.log
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.Closeable
import java.io.StringReader
import java.time.Instant
import java.util.concurrent.Executors
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

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
        fun <T> doSomethingIfSomethingOrElseSomethingElse(
            someCondition: () -> Boolean,
            doSomething: () -> T,
            doSomethingElse: () -> T
        ): T {
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

@Suppress("ConvertTryFinallyToUseCall")
internal class EnKlassMedEnSträngTest {

    @Test
    fun `foo bar`() {
        data class Person(val nane: String)

        val person = Person("Peter")

        fun Person.doStuff(block: (name: String) -> Unit) {
            println("nane = $nane")
            block("Lisa")
        }

        person.doStuff { println("Hello $it!") }

        fun Person.doStuff2(block: Person.() -> Unit) {
            println("nane = $nane")
            block()
        }

        person.doStuff2 { println("Hello $nane!") }
    }

    @Test
    fun `sfdgsd hsd`() {
        fun encloseInXMLAttribute(sb: StringBuilder, attr: String, action: (StringBuilder) -> StringBuilder): String {
            sb.append("<$attr>")
            action(sb)
            sb.append("</$attr>")
            return sb.toString()
        }

        val xml = encloseInXMLAttribute(StringBuilder(), "attr") {
            it.append("MyAttribute")
        }
        println(xml)

        fun encloseInXMLAttribute2(sb: StringBuilder, attr: String, action: StringBuilder.() -> StringBuilder): String {
            sb.append("<$attr>")
            sb.action()
            sb.append("</$attr>")
            return sb.toString()
        }

        val xml2 = encloseInXMLAttribute2(StringBuilder(), "attr") {
            append("MyAttribute")
        }
        println(xml2)
    }

    @Test
    fun `my own use function`() {
        fun <T : Closeable, R> T.closeIt(block: (T) -> R): R {
            try {
                return block(this)
            } finally {
                close()
            }
        }

        StringReader("fisksoppa").closeIt {
            assertThat(it.readText()).isEqualTo("fisksoppa")
        }
        assertThat(StringReader("fisksoppa").closeIt {
            it.readText()
        }).isEqualTo("fisksoppa")


        fun <T : Closeable, R> T.closeIt2(block: T.() -> R): R {
            try {
                return block()
            } finally {
                close()
            }
        }

        StringReader("fisksoppa").closeIt2 {
            assertThat(readText()).isEqualTo("fisksoppa")
        }
        assertThat(StringReader("fisksoppa").closeIt2 {
            readText()
        }).isEqualTo("fisksoppa")
    }
}

internal class PantDSLTest {

    enum class Hindertyp {
        ALLVARLIGT_FEL, KFM_SPARR
    }

    // TODO petves: typ -> enum
    data class Hinder(val id: String, val typ: Hindertyp)
    data class Pant(val id: String, val version: Int, val Hinder: List<Hinder>)

    /*
    Vi vill åstadkomma följande:
    val pant: Pant =
        pant {
            id: "p1"
            version: 1

            hinder {
                id: "h1"
                typ: "allvarligt fel"
            }
            hinder {
                id: "h2"
                typ: "KFM spärr"
            }
        }
     */

    class PantBuilder(var id: String? = null, var version: Int? = null, var hinder: ArrayList<Hinder> = ArrayList()) {
        fun build(): Pant = Pant(id!!, version!!, hinder)
    }

    class HinderBuilder(var id: String? = null, var typ: Hindertyp? = null) {
        fun build(): Hinder = Hinder(id!!, typ!!)
    }

    fun pant(block: PantBuilder.() -> Unit): Pant {
        val builder = PantBuilder()
        block(builder)
        return builder.build()
    }

    @Test
    fun `skapa pant utan hinder`() {
        assertThat(
            pant {
                id = "p1"
                version = 1
            }
        ).isEqualTo(Pant("p1", 1, emptyList()))
    }

    fun PantBuilder.hinder(block: HinderBuilder.() -> Unit) {
        val builder = HinderBuilder()
        block(builder)
        hinder.add(builder.build())
    }

    @Test
    fun `skapa pant med ett hinder`() {
        assertThat(
            pant {
                id = "p1"
                version = 1

                hinder {
                    id = "h1"
                    typ = ALLVARLIGT_FEL
                }
            }
        ).isEqualTo(Pant("p1", 1, listOf(Hinder("h1", ALLVARLIGT_FEL))))
    }

    @Test
    fun `skapa pant med två hinder`() {
        assertThat(
            pant {
                id = "p1"
                version = 1

                hinder {
                    id = "h1"
                    typ = ALLVARLIGT_FEL
                }
                hinder {
                    id = "h2"
                    typ = KFM_SPARR
                }
            }
        ).isEqualTo(Pant("p1", 1, listOf(Hinder("h1", ALLVARLIGT_FEL), Hinder("h2", KFM_SPARR))))
    }

    @Test
    fun `alphabet test`() {
        fun firstCharsOfAlphabet(count: Int): String {
            val stringBuilder = StringBuilder()
            stringBuilder.appendLine("First $count chars of the alphabet:")
            ('a'..'z').take(count).forEach { stringBuilder.append(it) }
            return stringBuilder.toString()
        }
        assertThat(firstCharsOfAlphabet(3)).isEqualTo("First 3 chars of the alphabet:\nabc")

        // Hmm, det var ett jäkla upprepande av sb ovan!

        fun firstCharsOfAlphabet2(count: Int): String {
            return with(StringBuilder()) {
                appendLine("First $count chars of the alphabet:")
                ('a'..'z').take(count).forEach { append(it) }
                toString()
            }
        }
        assertThat(firstCharsOfAlphabet2(3)).isEqualTo("First 3 chars of the alphabet:\nabc")

        fun firstCharsOfAlphabet3(count: Int): String {
            return StringBuilder().apply {
                appendLine("First $count chars of the alphabet:")
                ('a'..'z').take(count).forEach { append(it) }
            }.toString()
        }
        assertThat(firstCharsOfAlphabet3(3)).isEqualTo("First 3 chars of the alphabet:\nabc")

        // Hmm, bättre, men
    }

    @Test
    fun `do something with an Int`() {
        fun doSomethingWithAntInt(value: Int, action: (Int) -> Int): Int {
            return action(value)
        }

        assertThat(doSomethingWithAntInt(2) { it * 2 }).isEqualTo(4)
        assertThat(doSomethingWithAntInt(2) { it / 2 }).isEqualTo(1)
        assertThat(doSomethingWithAntInt(2) { it + 3 }).isEqualTo(5)
    }

    @Test
    fun `do something else with an Int`() {
        fun doSomethingWithAntInt(value: Int, action: Int.() -> Int): Int {
            return value.action()
        }

        assertThat(doSomethingWithAntInt(2) { this * 2 }).isEqualTo(4)
        assertThat(doSomethingWithAntInt(2) { this / 2 }).isEqualTo(1)
        assertThat(doSomethingWithAntInt(2) { this + 3 }).isEqualTo(5)

        assertThat(doSomethingWithAntInt(2) { this.compareTo(5) }).isLessThan(0)
        assertThat(doSomethingWithAntInt(2) { compareTo(5) }).isLessThan(0)
    }

    class TransactionManager {
        private val workList = ArrayList<Work>()

        data class Work(val descr: String)

        fun startTransaction() {
            println("Starting transaction")
        }

        fun addWork(work: Work) {
            workList.add(work)
        }

        fun commit() {
            printWorkList()
            println("Committed transaction")
        }

        private fun printWorkList() {
            println("Work list in transaction:")
            workList.forEach { println("\t$it") }
        }

        fun rollback() {
            printWorkList()
            println("Rollbacked transaction")
        }
    }

    @Test
    fun `do something in a transaction`() {
        val transactionManager = TransactionManager()

        transactionManager.startTransaction()
        try {
            transactionManager.addWork(Work("Hard work"))
            transactionManager.addWork(Work("Very hard work"))
            transactionManager.commit()
        } catch (e: Exception) {
            transactionManager.rollback()
        }
    }

    fun doSomethingInATransaction(block: (TransactionManager) -> Unit) {
        val transactionManager = TransactionManager()

        transactionManager.startTransaction()
        try {
            block(transactionManager)
            transactionManager.commit()
        } catch (e: Exception) {
            transactionManager.rollback()
        }
    }

    @Test
    fun `do something in a transaction2`() {
        doSomethingInATransaction {
            it.addWork(Work("Hard work"))
            it.addWork(Work("Very hard work"))
        }
    }

    fun doSomethingInATransaction2(block: TransactionManager.() -> Unit) {
        val transactionManager = TransactionManager()

        transactionManager.startTransaction()
        try {
            transactionManager.block()
            transactionManager.commit()
        } catch (e: Exception) {
            transactionManager.rollback()
        }
    }

    @Test
    fun `do something in a transaction with exception`() {
        doSomethingInATransaction2 {
            addWork(Work("Hard work"))
            addWork(Work("Very hard work"))
            throw RuntimeException("Something went really worng!")
        }
    }

    @Test
    fun `konkatenera listor`() {
        assertThat(listOf(1, 2, 3) + listOf(4, 5)).containsExactly(1, 2, 3, 4, 5)
        assertThat(listOf(1, 2, 3).plus(listOf(4, 5))).containsExactly(1, 2, 3, 4, 5)
    }


}

inline class InlinedClass(val bar: String)

class InlineFoo {
    fun takesInlinedClassAsAParam(inlinedClass: InlinedClass) = inlinedClass.bar

    fun returnslinedClassAsAParam() = InlinedClass("fossing")
}

class User(val username: String, val password: String)

/*
inline class Username(val username: String) {
    init {
        if(username.length < 10 && username.length > 20) throw RuntimeException("Username must be between 10 and 20 chars long")
    }
}
inline class Password(val password: String)
class StronglyTypedUser(val username: Username, val password: Password)
*/


typealias Username = String
typealias Password = String

class UserWithTypeAliases(val username: Username, val password: Password)

internal class InlineClassTest {
    val inlineFoo = mockk<InlineFoo>()

    @Test
    fun `as a param`() {
        every { inlineFoo.takesInlinedClassAsAParam(anyValue()) } returns "fisk"

        assertThat(inlineFoo.takesInlinedClassAsAParam(InlinedClass("bla"))).isEqualTo("fisk")
    }

    @Test
    fun `as result`() {
        val inlinedClass = InlinedClass("snorre")
        every { inlineFoo.returnslinedClassAsAParam() } returns value(inlinedClass)

        assertThat(inlineFoo.returnslinedClassAsAParam()).isEqualTo(inlinedClass)
    }

    @Test
    fun `inline class`() {
        val user = User("sven", "abc123")
    }

}

sealed class ResultOf<out R> {
    data class Success<out R>(val value: R) : ResultOf<R>()
    data class Failure(
        val message: String?,
        val throwable: Throwable?
    ) : ResultOf<Nothing>() {
        init {
            require(message == null && throwable == null) { "At least one of message and throwable has to be non-null" }
        }
    }
}

sealed class ResultOf2<out R> {
    data class Success<out R>(val value: R) : ResultOf2<R>()
    data class NumberFailure(val message: String?) : ResultOf2<Nothing>()
    data class MegaFailure(val message: String?) : ResultOf2<Nothing>()
    object UnknownErrorFailure : ResultOf2<String>()
}


internal class SealedClassTest {

    val <T> T.exhaustive: T
        get() = this

    @Test
    fun `asdfasd ga sdgas`() {
        fun canBeAFailure(boolean: Boolean): ResultOf<String> {
            return if (boolean) ResultOf.Success("Jaaa!") else ResultOf.Failure(
                "Hä gick på skit",
                RuntimeException("Blä")
            )
        }

        assertThat(canBeAFailure(true)).isEqualTo(ResultOf.Success("Jaaa!"))
        assertThat(canBeAFailure(false)).isEqualToIgnoringGivenFields(
            ResultOf.Failure(
                "Hä gick på skit",
                RuntimeException("Blä")
            ), "throwable"
        )
    }

    @Test
    fun `dgsdf ywet7w46`() {
        fun canBeAFailure(value: Int): ResultOf2<String> {
            return when (value) {
                1 -> ResultOf2.Success("1")
                2 -> ResultOf2.NumberFailure("2")
                else -> ResultOf2.MegaFailure("Shit!")
            }
        }

        assertThat(canBeAFailure(1)).isEqualTo(ResultOf2.Success("1"))
        assertThat(canBeAFailure(2)).isEqualTo(ResultOf2.NumberFailure("2"))
        assertThat(canBeAFailure(2626)).isEqualTo(ResultOf2.MegaFailure("Shit!"))

        when (canBeAFailure(1)) {
            is ResultOf2.Success -> println("Det gick bra!")
            is ResultOf2.NumberFailure -> println("Vi fick nummerfel...")
            is ResultOf2.MegaFailure -> println("Vi fick megafel... :/")
            is ResultOf2.UnknownErrorFailure -> println("Vi har ingen aning vad som gick snett...")
        }.exhaustive
    }
}

internal class ExhaustiveSealedClassTest {

    abstract sealed class Fordon(open val namn: String, open val antalHjul: Int) {
        data class Cykel(override val namn: String, override val antalHjul: Int) : Fordon(namn, antalHjul)
        data class Cross(override val namn: String, override val antalHjul: Int) : Fordon(namn, antalHjul)
        data class Gokart(override val namn: String, override val antalHjul: Int) : Fordon(namn, antalHjul)
    }

    @Test
    fun `when som uttryck`() {
        val fordon: Fordon = Fordon.Cykel("Min velociped", 2)

        val typ = when (fordon) {
            is Fordon.Cykel -> "En cykel"
            is Fordon.Cross -> "En cross"
            is Fordon.Gokart -> "En gokart"
        }

        assertThat(typ).isEqualTo("En cykel")
    }

    @Test
    fun `when utan uttryck`() {
        val fordon: Fordon = Fordon.Cross("Lera är kul!", 2)

        var typ: String? = null

        fun setTyp(value: String) {
            typ = value
        }

        when (fordon) {
            //is Fordon.Cykel -> setTyp("En cykel")
            is Fordon.Cross -> setTyp("En cross")
            is Fordon.Gokart -> setTyp("En gokart")
        }

        assertThat(typ).isEqualTo("En cross")
    }

    val <T> T.exhaustive: T
        get() = this

    @Test
    fun `when utan uttryck men som exhaustive mha extension property`() {
        val fordon: Fordon = Fordon.Cross("Lera är kul!", 2)

        var typ: String? = null

        fun setTyp(value: String) {
            typ = value
        }

        when (fordon) {
            is Fordon.Cykel -> setTyp("En cykel")
            is Fordon.Cross -> setTyp("En cross")
            is Fordon.Gokart -> setTyp("En gokart")
        }.exhaustive

        assertThat(typ).isEqualTo("En cross")
    }

    object Be {
        inline infix fun <reified T> exhaustive(any: T?) = any
    }

    @Test
    fun `when utan uttryck men som exhaustive mha funktion`() {
        val fordon: Fordon = Fordon.Gokart("Asfalt är kul!", 2)

        var typ: String? = null

        fun setTyp(value: String) {
            typ = value
        }

        Be exhaustive when (fordon) {
            is Fordon.Cykel -> setTyp("En cykel")
            is Fordon.Cross -> setTyp("En cross")
            is Fordon.Gokart -> setTyp("En gokart")
        }.exhaustive

        assertThat(typ).isEqualTo("En gokart")
    }
}

internal class ApplyTest {

    data class Person(val name: String, val age: Int, val children: List<Person> = emptyList())

    class PersonBuilder {
        var name: String? = null
        var age: Int? = null
        var children: ArrayList<Person> = ArrayList()

        fun addChild(child: Person) {
            children.add(child)
        }

        // TODO: Felhantering
        fun build(): Person = Person(name!!, age!!, children)
    }

    @Test
    fun `create person`() {
        val builder = PersonBuilder()
        builder.name = "Sven Stare"
        builder.age = 56
        builder.addChild(Person("Nina Stare", 26))
        builder.addChild(Person("Mikael Stare", 24))

        val person = builder.build()
    }

    @Test
    fun `create person with apply`() {
        val person = PersonBuilder().apply {
            name = "Sven Stare"
            age = 56
            addChild(Person("Nina Stare", 26))
            addChild(Person("Mikael Stare", 24))
        }.build()
    }

    @Test
    fun `test foo`() {
        class Person(var firstName: String, var lastName: String) {
            val fullName
                get() = "$firstName $lastName"
        }

        val person = Person("Dummer", "Jöns")
        assertThat(person.fullName).isEqualTo("Dummer Jöns")

        person.firstName = "Sune"
        assertThat(person.fullName).isEqualTo("Sune Jöns")
    }
}

internal class FooTest {

    fun square(int: Int): Int {
        log("Jag beräknar kvadraten på $int")
        Thread.sleep(1000)  // Simulerar en tidskrävande beräkning
        return int * int
    }

    @Test
    fun `sekventiellt`() {
        val exekveringstid = measureTimeMillis {
            log("Kvadraten på 4 är ${square(4)}")
            log("Kvadraten på 5 är ${square(5)}")
        }
        log("Total exekveringstid: $exekveringstid ms")
    }

    @Test
    fun `parallellt mha trådar`() {
        val exekveringstid = measureTimeMillis {
            thread { log("Kvadraten på 4 är ${square(4)}") }
            thread { log("Kvadraten på 4 är ${square(5)}") }
        }
        log("Total exekveringstid: $exekveringstid ms")
    }

    @Test
    fun `parallellt mha trådar och Futures`() {
        // Skapa en trådpool med 7 trådar
        val executor = Executors.newFixedThreadPool(7)

        val exekveringstid = measureTimeMillis {
            val future4 = executor.submit { log("Kvadraten på 4 är ${square(4)}") }
            val future5 = executor.submit { log("Kvadraten på 5 är ${square(5)}") }

            future4.get()
            future5.get()
        }
        log("Total exekveringstid: $exekveringstid ms")
    }
}