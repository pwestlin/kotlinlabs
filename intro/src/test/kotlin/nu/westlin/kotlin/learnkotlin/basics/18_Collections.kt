@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package nu.westlin.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Locale

class CollectionsTest {
    val fruits = listOf("banana", "avocado", "apple", "kiwifruit", "orange")

    @Test
    fun `get and set items`() {
        val cars = mutableListOf("Volvo", "Saab", "Opel")

        // get
        assertThat(cars[1]).isEqualTo("Saab")
        assertThat(cars[1] == cars.get(1)).isTrue     // get (och set) funkar också men ska inte användas

        // set
        cars[1] = "Snabel"
        assertThat(cars[1]).isEqualTo("Snabel")
    }

    @Test
    fun `mutable and immutable`() {
        val numbers = listOf(1, 2, 3)
        assertThat(numbers).containsExactly(1, 2, 3)
        //numbers.add(4)    // add finns inte som funktion på en immutable list

        val numbers2 = mutableListOf(1, 2, 3)
        assertThat(numbers2).containsExactly(1, 2, 3)
        numbers2.add(4)
        assertThat(numbers2).containsExactly(1, 2, 3, 4)
    }


    @Test
    fun `if - else if - else`() {
        when {
            "orange" in fruits -> println("juicy")
            "apple" in fruits -> println("apple is fine too")
            else -> println("whatever")
        }

    }

    @Test
    fun `first and last`() {
        //first/last (ni vet där man i Java skriver fruits.get(0) och fruits.get(fruits.size() -1)

        assertThat(fruits.first()).isEqualTo("banana")
        assertThat(fruits.last()).isEqualTo("orange")
    }

    @Test
    fun count() {
        assertThat(fruits.count()).isEqualTo(5)
        assertThat(fruits.count { it.contains("a") }).isEqualTo(4)
    }

    @Test
    fun `firstOrNull and lastOrNull`() {
        val list = mutableListOf(1, 2)
        assertThat(list.firstOrNull()).isEqualTo(1)
        assertThat(list.lastOrNull()).isEqualTo(2)

        list.clear()
        assertThat(list.firstOrNull()).isNull()
        assertThat(list.lastOrNull()).isNull()
    }

    @Test
    fun `filter`() {
        val fruitsWithA = fruits            // Inget anrop till stream()
            .filter { it.startsWith("a") }      // "it" är default-namnet för en ensam parameter i strömmar
        // Inget anrop till .collect(Collectors.toList())
        assertThat(fruitsWithA).containsExactlyInAnyOrder("avocado", "apple")
    }

    @Test
    fun `map`() {
        val fruitsWithA = fruits            // Inget anrop till stream()
            //.filter { s -> s.startsWith("a") }
            .filter { it.startsWith("a") }      // "it" är default-namnet för en ensam parameter i strömmar
            .sortedBy { it }
            .map { it.uppercase(Locale.getDefault()) }
        // Inget anrop till .collect(Collectors.toList())
        assertThat(fruitsWithA).containsExactlyInAnyOrder("AVOCADO", "APPLE")
    }

    @Test
    fun `list + value`() {
        val strings = listOf("a", "b")
        val string = "c"

        assertThat(strings + string).containsExactly("a", "b", "c")
    }
    
    @Test
    fun `list + list`() {
        val strings1 = listOf("a", "b")
        val strings2 = listOf("c", "d")

        assertThat(strings1 + strings2).containsExactly("a", "b", "c", "d")
    }
}

class UnionIntersectAndSubtractTest {

    private val parents = listOf("Matt", "Peter", "Dana", "Lisa")
    private val children = listOf("Scott", "Peter", "Diane", "Lisa")

    @Test
    fun `union test`() {
        assertThat(parents union children).containsExactlyInAnyOrder("Matt", "Peter", "Dana", "Lisa", "Scott", "Diane")
    }

    @Test
    fun `intersect test`() {
        assertThat(parents intersect children).containsExactlyInAnyOrder("Peter", "Lisa")
    }

    @Test
    fun `subtract test`() {
        assertThat(parents subtract children).containsExactlyInAnyOrder("Matt", "Dana")
        assertThat(parents - children).containsExactlyInAnyOrder("Matt", "Dana")
    }
}