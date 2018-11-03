@file:Suppress("unused")

package se.lantmateriet.taco.kotlin.application.jaxb

import org.springframework.stereotype.Component
import java.io.StringWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.helpers.DefaultValidationEventHandler

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Movie(val name: String, val year: Int) {

    // Nedanstående konstruktor är för att JAXB kräver det
    // Först: Att tvingas ha en no-arg-konstruktor suger häst.
    // Sedan: Att tvingas skicka in data till fält är ju bara heldumt. :/
    private constructor() : this(name = "foo", year = 1)
}

@Component
class XmlMapper {
    private val marshaller: Marshaller
    private val unmarshaller: Unmarshaller

    init {
        val jaxbContext = JAXBContext.newInstance(Movie::class.java)
        this.marshaller = jaxbContext.createMarshaller()
        this.marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        //this.marshaller.setProperty(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)

        this.unmarshaller = jaxbContext.createUnmarshaller()
        this.unmarshaller.eventHandler = DefaultValidationEventHandler()
        //this.unmarshaller.setProperty(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)
    }

    fun writeXml(any: Any): String {
        return StringWriter().use {
            marshaller.marshal(any, it)
            it.toString()
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> from(string: String): T {
        return string.reader().use {
            unmarshaller.unmarshal(it) as T
        }
    }
}