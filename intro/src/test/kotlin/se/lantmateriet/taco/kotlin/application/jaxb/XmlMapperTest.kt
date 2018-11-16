package se.lantmateriet.taco.kotlin.application.jaxb

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class XmlMapperTest {
    val xmlMapper = XmlMapper()
    val movie = ActionMovie(-1, "Top Secret!", 1984)

    @Test
    fun `to and from XML`() {
        val xml = xmlMapper.writeXml(movie)
        println("xml = $xml")
        val movieFromXml = xmlMapper.from<ActionMovie>(xml)

        assertThat(movieFromXml.name).isEqualTo(movie.name)
        assertThat(movieFromXml.year).isEqualTo(movie.year)
    }
}