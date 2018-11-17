@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.system.measureNanoTime

// https://blog.kotlin-academy.com/effective-kotlin-use-sequence-for-bigger-collections-with-more-than-one-processing-step-649a15bb4bf

class SequencesTest {

    @Test
    fun `map a collection`() {
        var mapCount = 0
        var filterCount = 0
        val result = listOf(1, 2, 3, 4, 5)
            .map { n ->
                println("mapping $n")
                mapCount++
                n * n }
            .filter {n ->
                println("filtering $n")
                filterCount++
                n < 10 }
            .first()

        assertThat(mapCount).isEqualTo(5)
        assertThat(filterCount).isEqualTo(5)
    }

    @Test
    fun `map a collection using sequence`() {
        var mapCount = 0
        var filterCount = 0
        val result = listOf(1, 2, 3, 4, 5).asSequence()
            .map { n ->
                println("mapping $n")
                mapCount++
                n * n }
            .filter {n ->
                println("filtering $n")
                filterCount++
                n < 10 }
            .first()

        assertThat(mapCount).isEqualTo(1)
        assertThat(filterCount).isEqualTo(1)
    }

    @Test
    fun sequence() {
        println("Sequence:")
        sequenceOf(1, 2, 3)
            .filter { println("Filter $it, "); it % 2 == 1 }
            .map { println("Map $it, "); it * 2 }
            .toList()
        // Prints: Filter 1, Map 1, Filter 2, Filter 3, Map 3,

        println()
        println("Iterator:")
        listOf(1, 2, 3)
            .filter { println("Filter $it, "); it % 2 == 1 }
            .map { println("Map $it, "); it * 2 }
        // Prints: Filter 1, Filter 2, Filter 3, Map 1, Map 3,
    }

    class Product(val bought: Boolean, val price: Int)

    val productsList = createProducts(100000)

    private fun createProducts(number: Int): List<Product> {
        fun bought() = Random.nextInt(0..1) == 1
        fun price() = Random.nextInt(1..100)

        val products = mutableListOf<Product>()
        repeat(number) {
            products.add(Product(bought(), price()))
        }

        return products
    }


    fun singleStepListProcessing(): List<Product> {
        return productsList.filter { it.bought }
    }

    fun singleStepSequenceProcessing(): List<Product> {
        return productsList.asSequence()
            .filter { it.bought }
            .toList()
    }

    fun twoStepListProcessing(): List<Int> {
        return productsList
            .filter { it.bought }
            .map { it.price }
    }

    fun twoStepSequenceProcessing(): List<Int> {
        return productsList.asSequence()
            .filter { it.bought }
            .map { it.price }
            .toList()
    }

    fun threeStepListProcessing(): Double {
        return productsList
            .filter { it.bought }
            .map { it.price }
            .average()
    }

    fun threeStepSequenceProcessing(): Double {
        return productsList.asSequence()
            .filter { it.bought }
            .map { it.price }
            .average()
    }

    @Test
    fun `process products with single step processing`() {
        val list = measureNanoTime { singleStepListProcessing() } / productsList.size
        val sequence = measureNanoTime { singleStepSequenceProcessing() } / productsList.size
        println("List:$list ns")
        println("Sequence:$sequence ns")
    }

    @Test
    fun `process products with two step processing`() {
        val list = measureNanoTime { twoStepListProcessing() } / productsList.size
        val sequence = measureNanoTime { twoStepSequenceProcessing() } / productsList.size
        println("List:$list ns")
        println("Sequence:$sequence ns")
    }

    @Test
    fun `process products with three step processing`() {
        val list = measureNanoTime { threeStepListProcessing() } / productsList.size
        val sequence = measureNanoTime { threeStepSequenceProcessing() } / productsList.size
        println("List:$list ns")
        println("Sequence:$sequence ns")
    }
}