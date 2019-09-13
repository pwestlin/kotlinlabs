package nu.westlin.kotlin.dsl

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate

class PersonTest {

    @Test
    fun `create a person without adresses`() {
        val person = person {
            name = "John"
            dateOfBirth = "1980-12-01"
        }

        with(person) {
            assertThat(name).isEqualTo("John")
            assertThat(dateOfBirth).isEqualTo(LocalDate.parse("1980-12-01"))
        }
        assertThat(person.addresses).isEmpty()
    }

    @Test
    fun `create a person with two adresses`() {
        val person = person {
            name = "John Doe"
            dateOfBirth = "1980-12-01"

            nickName { "Jay" }
            nickName { "The D" }

            address {
                street = "Main Street"
                number = 12
                city = "London"
            }
            address {
                street = "High Street"
                number = 7
                city = "New York"
            }
        }

        with(person) {
            assertThat(name).isEqualTo("John Doe")
            assertThat(nickNames).containsExactlyInAnyOrder("Jay", "The D")
            assertThat(dateOfBirth).isEqualTo(LocalDate.parse("1980-12-01"))

            assertThat(addresses).containsExactlyInAnyOrder(
                Address(
                    street = "Main Street",
                    number = 12,
                    city = "London"
                ),
                Address(
                    street = "High Street",
                    number = 7,
                    city = "New York"
                )
            )
        }
    }
}