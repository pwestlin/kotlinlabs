@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName", "UNUSED_EXPRESSION", "UnusedEquals", "ReplaceSingleLineLet")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.io.File
import java.io.FileInputStream
import java.util.Properties


class LessBasicTest {


    @Test
    fun `TODO`() {
        assertThatThrownBy { TODO("Implementera mig då!") }
            .isInstanceOf(NotImplementedError::class.java)
            .hasMessage("An operation is not implemented: Implementera mig då!")
    }


    object Resource {
        const val name = "I am a singleton"
        const val value = 42
    }

    @Test
    fun singleton() {
        assertThat(Resource.name).isEqualTo("I am a singleton")
        assertThat(Resource.value).isEqualTo(42)
    }

    @Test
    fun `infix member function`() {
        class Person(name: String) {
            val likedStruff = mutableListOf<String>()
            infix fun likes(thing: String) {
                likedStruff.add(thing)
            }
        }

        val peter = Person("Peter")
        peter likes "Kotlin"
        peter likes "Karting"

        assertThat(peter.likedStruff).containsExactlyInAnyOrder("Karting", "Kotlin")
    }

    @Test
    fun `infix extension function`() {
        infix fun Int.add(i: Int) = this + i

        assertThat(7.add(3)).isEqualTo(10)
        assertThat(7 add 3).isEqualTo(10)
    }

    // infix verkar bäst för tester


    /*
    In Kotlin, unlike Java or C#, classes do not have static methods. In most cases, it's recommended to simply use package-level functions instead.
    If you need to write a function that can be called without having a class instance but needs access to the internals of a class (for example, a factory method),
    you can write it as a member of an object declaration inside that class.
    Even more specifically, if you declare a companion object inside your class, you'll be able .to
    call its members with the same syntax as calling static methods in Java/C#, using only the class name as a qualifier.
     */

    class Person private constructor(val name: String) {
        companion object Factory {
            fun create(firstname: String, surname: String): Person = Person("$firstname $surname")
        }
    }

    @Test
    fun `companion objects`() {
        val myClass = Person.create("Pelle", "Plutt")

        assertThat(myClass.name).isEqualTo("Pelle Plutt")
    }

    @Test
    fun apply() {
        val dir = System.getProperty("java.io.tmpdir") + "/bulle"
        val createdDir = File(dir).apply { mkdirs() }
        assertThat(File(System.getProperty("java.io.tmpdir") + "/bulle")).exists()

        val i = 7.apply { minus(3) }
        assertThat(i).isEqualTo(7)      // Varför 7 och inte 4?
    }

    @Test
    fun `operator functions`() {
        operator fun Int.times(str: String) = str.repeat(this)
        assertThat(2 * "Bye ").isEqualTo("Bye Bye ")

        operator fun String.get(range: IntRange) = substring(range)  // 3
        val str = "Always forgive your enemies; nothing annoys them so much."
        assertThat(str[0..13]).isEqualTo("Always forgive")
    }

    @Test
    fun `destructuring declarations`() {
        data class User(val username: String, val email: String)

        val user = User("bosseringholmo", "bosse_man@oldfolkshome.se")

        val (username, email) = user
        assertThat(username).isEqualTo("bosseringholmo")
        assertThat(email).startsWith("bosse_man")
    }

    @Test
    fun let() {
        class Foo(arg: String)

        // let returnerar det sista "uttrycket" inom {}
        assertThat(Foo("foo").let {
            true
            false
        }).isFalse()

        val let = Foo("Bar").let { foo ->
            foo.toString()

            // Do some stuff
            // Do more stuff
            // Do some more stuff

            foo == Foo("Bra")
        }
        assertThat(let).isFalse()

    }

    @Test
    fun also() {
        class Foo(arg: String)

        val foo = Foo("foo")

        // also returnerar "this", dvs foo i fallet nedan
        assertThat(foo.also {
            true
            false
        }).isEqualTo(foo)

        assertThat(
            foo.also {
                foo.toString()

                // Do some stuff
                // Do more stuff
                // Do some more stuff

                foo == Foo("Bra")
            }).isEqualTo(foo)
    }

    @Test
    fun `combine let and also to create a dir`() {
        val path = System.getProperty("java.io.tmpdir") + "/foo/bar/foobar"
        try {
            assertThat(File(path)).doesNotExist()

            path
                .let { File(it) }        // it = path (från variabel ovan)
                .also { it.mkdirs() }    // it = file (från let ovan)

            assertThat(File(path)).exists()
        } finally {
            File(path).delete()
        }
    }

    @Test
    fun `try with resources`() {

/*
    // Java 1.7 and above
    Properties prop = new Properties();
    try (FileInputStream fis = new FileInputStream("config.properties")) {
        prop.load(fis);
    }
    // fis automatically closed
 */


        val prop = Properties()
        try {
            FileInputStream("config.properties").use {
                prop.load(it)
            }
            // FileInputStream automatically closed
        } catch (e: Throwable) {
            // Felhantering
        }
    }


    fun measureTimeInMilliSeconds(block: () -> Unit): Long {
        val start = System.currentTimeMillis()
        block()
        return System.currentTimeMillis() - start
    }

    @Test
    fun `measure time`() {
        val execTime = measureTimeInMilliSeconds {
            var counter = 0
            for (i in 1..5_000_000_000) {
                counter += 1
            }
        }

        println("Exec time: $execTime ms")
    }
}