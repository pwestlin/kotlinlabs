package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.compositionoverinheritance.inheritance

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private class CounterSet<T>: HashSet<T>() {

    var elementsAdded: Int = 0
        private set

    override fun add(element: T): Boolean {
        elementsAdded++
        return super.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elementsAdded += elements.size
        return super.addAll(elements)
    }
}

internal class CounterSetTest {

    @Test
    fun `add - elementsAdded should be 2`() {
        val set = CounterSet<Int>()
        set.add(1)
        set.add(2)

        assertThat(set.elementsAdded).isEqualTo(2)
    }

    @Test
    fun `addAll - elementsAdded should be 2`() {
        val set = CounterSet<Int>()

        set.addAll(setOf(1, 2))

        assertThat(set.elementsAdded).isEqualTo(2)
    }
}