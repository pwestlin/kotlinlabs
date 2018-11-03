package se.lantmateriet.taco.kotlin.fastighet

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable
import java.util.UUID
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

@XmlRootElement
@XmlType(propOrder = arrayOf("id", "objektidentitet", "objektversion", "versionGiltigFran", "versionGiltigTill"))
abstract class Utbytesobjekt<T : Utbytesobjekt<T>>(val objektidentitet: Objektidentitet) {

    /**
     * "id", eller mer korrekt "gml:id", används ej i domänen utan krävs av XSD:n.
     */
    @XmlAttribute(namespace = "http://www.opengis.net/gml/3.2")
    private val id = Objektidentitet.randomObjektidentitet()
}

@XmlRootElement(name = "Kommun")
@XmlType(propOrder = arrayOf("lanskod", "kommunkod", "kommunnamn", "goStatus", "upphortDatum", "lansreferens"))
class Kommun(objektidentitet: Objektidentitet, val kommunnamn: String) : Utbytesobjekt<Kommun>(objektidentitet)

class FastighetService {
    fun doSomething(utbytesobjekt: Utbytesobjekt<*>) {
        println("utbytesobjekt.objektidentitet = ${utbytesobjekt.objektidentitet}")
        println("utbytesobjekt.javaClass = ${utbytesobjekt.javaClass}")
    }
}


@XmlRootElement
class Objektidentitet private constructor(@get:JsonValue val uuid: UUID) : Serializable {

    override fun toString(): String {
        return this.uuid.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Objektidentitet

        if (uuid != other.uuid) return false

        return true
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }

    companion object {

        @JsonCreator
        fun from(value: String): Objektidentitet {
            return Objektidentitet(UUID.fromString(value))
        }

        fun randomObjektidentitet(): Objektidentitet {
            return Objektidentitet(UUID.randomUUID())
        }
    }

}
