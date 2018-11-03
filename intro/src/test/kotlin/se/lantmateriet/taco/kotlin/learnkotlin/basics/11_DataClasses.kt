@file:Suppress("UNUSED_VARIABLE", "UNUSED_VALUE", "RedundantExplicitType", "ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE", "VARIABLE_WITH_REDUNDANT_INITIALIZER", "ALWAYS_NULL", "UNNECESSARY_SAFE_CALL", "EXPERIMENTAL_FEATURE_WARNING", "MemberVisibilityCanBePrivate", "SimplifyBooleanWithConstants", "ConstantConditionIf", "MoveLambdaOutsideParentheses", "UnnecessaryVariable", "unused", "UNUSED_PARAMETER", "RemoveRedundantBackticks", "NullChecksToSafeCall", "LiftReturnOrAssignment", "ReplaceGetOrSet", "NonAsciiCharacters", "PackageName", "ClassName")

package se.lantmateriet.taco.kotlin.learnkotlin.basics

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataClassesTest {

    @Test
    fun `classes and objects - data classes`() {
        data class Person(val name: String, val age: Int)

        val person = Person("Peter", 46)
        println("person = ${person}")
        println("${person.name} är ${person.age} år gam...ung")

        assertThat(person.name).isEqualTo("Peter")
        assertThat(person.age).isEqualTo(46)

        val person2 = person.copy()
        // equals (and hashCode) är automatgenererade
        assertThat(person2 == person).isTrue()
        assertThat(person2 === person).isFalse()

        val person3 = person2.copy(age = 79)
        // equals (and hashCode) är automatgenererade
        assertThat(person3.age).isEqualTo(79)
        assertThat(person3 == person2).isFalse()
        assertThat(person3 === person2).isFalse()
    }


}