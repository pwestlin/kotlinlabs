package nu.westlin.kotlin.database

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class DatabaseTest {
    val database = Database("Ace of Base")

    class Foo(val foo: String, val bar: Int)

    @Test
    fun `create table with internal type`() {
        val tablename = "strings"
        database.createTable<String>(tablename)

        assertThat(database.listTablenames()).containsExactly(tablename)
        assertThat(database.getTable(tablename)).isNotNull
    }

    @Test
    fun `create table with custom type`() {
        val tablename = "foos"

        database.createTable<Foo>(tablename)
        assertThat(database.listTablenames()).containsExactly(tablename)
        assertThat(database.getTable(tablename)).isNotNull

    }

    @Test
    fun `fg adh asd`() {
        class Foo {
            val lists = arrayListOf<List<Any>>()

            fun add(list: List<Any>) {
                lists.add(list)
            }
        }

        val foo = Foo()
        val list = listOf(1, 2, 3)
        foo.add(list)
        foo.add(listOf("a", "b", "c"))

        val list1 = foo.lists[0]
        assertThat(list1).isEqualTo(list)

        foo.lists.forEach {
            println("it = ${it.javaClass}")
        }
    }

}