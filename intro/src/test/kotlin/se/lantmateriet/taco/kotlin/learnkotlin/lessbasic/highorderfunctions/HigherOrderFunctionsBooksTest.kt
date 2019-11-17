package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.highorderfunctions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

private data class Book(val name: String = "The Book", val price: Double = 22.55, val weight: Double = 0.68)
private typealias BookMapper<T> = (Book) -> T

class BooksTest {

    private val book1 = Book("Another book", 1.11, 2.11)
    private val book2 = Book("Another book", 2.22, 3.22)
    private val book3 = Book("Another book", 3.33, 4.33)

    private val books = listOf(book1, book2, book3)

    @Test
    fun `total extension function`() {
        fun List<Book>.total(f: (Book) -> Double): Double = fold(0.0) { total, book -> total + f(book) }

        assertThat(books.total { it.price }).isEqualTo(6.66)
        assertThat(books.total { it.weight }).isEqualTo(9.66)
    }

    @Test
    fun `total extension function with typealias`() {
        fun List<Book>.total(f: BookMapper<Double>): Double = fold(0.0) { total, book -> total + f(book) }

        assertThat(books.total { it.price }).isEqualTo(6.66)
        assertThat(books.total { it.weight }).isEqualTo(9.66)
    }
}
