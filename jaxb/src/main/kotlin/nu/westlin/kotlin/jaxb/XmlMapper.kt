package nu.westlin.kotlin.jaxb

import org.eclipse.persistence.jaxb.BeanValidationMode
import org.eclipse.persistence.jaxb.MarshallerProperties
import java.io.ByteArrayOutputStream
import java.io.Reader
import java.io.StringReader
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller
import javax.xml.bind.helpers.DefaultValidationEventHandler

class XmlMapper {
    private val marshaller: Marshaller
    private val unmarshaller: Unmarshaller

    init {
        try {
            val jaxbContext = JAXBContext.newInstance(Status::class.java)

            marshaller = jaxbContext.createMarshaller()
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
            marshaller.setProperty(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)

            unmarshaller = jaxbContext.createUnmarshaller()
            unmarshaller.eventHandler = DefaultValidationEventHandler()
            unmarshaller.setProperty(MarshallerProperties.BEAN_VALIDATION_MODE, BeanValidationMode.NONE)
        } catch (e: JAXBException) {
            throw RuntimeException("Kunde inte instansiera JAXB", e)
        }

    }

    fun writeXml(o: Any): String {
        val s = ByteArrayOutputStream()
        try {
            this.marshaller.marshal(o, s)
        } catch (e: JAXBException) {
            throw RuntimeException("Kunde inte konvertera " + o.javaClass.name + " till XML", e)
        }

        return s.toString()
    }

    @Throws(JAXBException::class)
    fun <T> from(string: String): T {
        return this.from(StringReader(string))
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(JAXBException::class)
    fun <T> from(reader: Reader): T {
        val o = this.unmarshaller.unmarshal(reader)
        return o as T
    }
}