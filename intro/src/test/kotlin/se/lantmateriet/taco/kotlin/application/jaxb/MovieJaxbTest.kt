package se.lantmateriet.taco.kotlin.application.jaxb

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.StringWriter
import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller

class MovieJaxbTest {

    val logger = LoggerFactory.getLogger(this.javaClass)!!

    @Test
    fun `marshall and unmarshal a movie`() {
        val movie = Movie("Top Secret!", 1984)

        val jaxbContext = JAXBContext.newInstance(Movie::class.java)
        val marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)

        val stringWriter = StringWriter()
        marshaller.marshal(movie, stringWriter)

        val movieStr = stringWriter.toString()
        logger.debug(movieStr)

        val unmarshaller = jaxbContext.createUnmarshaller()
        movieStr.reader().use {
            val movieFromString = unmarshaller.unmarshal(it) as Movie
            logger.debug("$movieFromString")
            with(movieFromString) {
                assertThat(name).isEqualTo("Top Secret!")
                assertThat(year).isEqualTo(1984)
            }
        }
    }
}