package se.lantmateriet.taco.kotlin.application.jaxb

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.inject.Inject

@RunWith(SpringRunner::class)
@SpringBootTest
class XmlMapperTest {

    @Inject
    lateinit var xmlMapper: XmlMapper

    val movie = Movie("Top Secret!", 1984)

    @Test
    fun `to and from XML`() {
        val xml = xmlMapper.writeXml(movie)
        println("xml = ${xml}")
        val movieFromXml = xmlMapper.from<Movie>(xml)

        assertThat(movieFromXml.name).isEqualTo(movie.name)
        assertThat(movieFromXml.year).isEqualTo(movie.year)
    }
}