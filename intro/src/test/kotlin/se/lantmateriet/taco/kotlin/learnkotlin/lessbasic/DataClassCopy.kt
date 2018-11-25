@file:Suppress("RemoveRedundantBackticks")

package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/*
The idea here is to test how deep a copy of a data class goes.
 */

abstract class RegularClassA {
    val aList = listOf("regular A foo", "regular A bar")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RegularClassA

        if (aList != other.aList) return false

        return true
    }

    override fun hashCode(): Int {
        return aList.hashCode()
    }


}

class RegularClassB : RegularClassA() {
    val bList = listOf("regular B foo", "regular B bar")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as RegularClassB

        if (bList != other.bList) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + bList.hashCode()
        return result
    }


}

data class DataClassA(val a: String) {
    val stringList = listOf("data foo", "data bar")
    val reg1List = listOf(RegularClassB(), RegularClassB())
}

data class DataClassB(val b: String) {
    val bList = listOf(DataClassA("A1"), DataClassA("A2"))
}

data class DataClassC(val b: String): RegularClassA() {
    val cList = listOf("a", "b")
}

class DataClassCopyTest {

    @Test
    fun `DataClassA`() {
        val original = DataClassA("foo")
        val copy = original.copy()

        assertThat(copy == original).isTrue()
        assertThat(copy === original).isFalse()

        assertThat(copy.stringList == original.stringList).isTrue()
        assertThat(copy.stringList === original.stringList).isFalse()

        assertThat(copy.reg1List == original.reg1List).isTrue()
        assertThat(copy.reg1List === original.reg1List).isFalse()
    }

    @Test
    fun `DataClassC`() {
        val original = DataClassC("foo")
        val copy = original.copy()

        assertThat(copy == original).isTrue()
        assertThat(copy === original).isFalse()

        assertThat(copy.cList == original.cList).isTrue()
        assertThat(copy.cList === original.cList).isFalse()

        assertThat(copy.aList == original.aList).isTrue()
        assertThat(copy.aList === original.aList).isFalse()
    }
}