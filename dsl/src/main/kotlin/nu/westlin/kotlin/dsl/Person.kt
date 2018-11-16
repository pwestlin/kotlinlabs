package nu.westlin.kotlin.dsl

import java.time.LocalDate

data class Person(
    val name: String,
    val dateOfBirth: LocalDate,
    var addresses: List<Address>
)

data class Address(
    val street: String,
    val number: Int,
    val city: String
)

@DslMarker
annotation class PersonDsl

@PersonDsl
class PersonBuilder {

    var name: String = ""

    var dateOfBirth: String = ""

    private var addresses =  mutableListOf<Address>()
    fun address(block: AddressBuilder.() -> Unit) {
        addresses.add(AddressBuilder().apply(block).build())
    }

    fun build(): Person = Person(name, LocalDate.parse(dateOfBirth), addresses)
}

@PersonDsl
class AddressBuilder {
    var street: String = ""
    var number: Int = 0
    var city: String = ""
    fun build(): Address = Address(street, number, city)
}

fun person(block: PersonBuilder.() -> Unit): Person = PersonBuilder().apply(block).build()
