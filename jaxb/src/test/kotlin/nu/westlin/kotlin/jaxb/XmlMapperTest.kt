package nu.westlin.kotlin.jaxb

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test


class XmlMapperTest {
    val xmlMapper = XmlMapper()

    @Test
    fun status() {
        val status = Status(1, "finns")
        println("status = ${status}")

        val xml = xmlMapper.writeXml(status)
        println("xmlMapper.writeXml(status) = $xml")

        val statusFromXml = xmlMapper.from<Status>(xml)
        println("statusFromXml = $statusFromXml")
        //assertThat(statusFromXml).isEqualTo(status)
        assertThat(statusFromXml.id).isEqualTo(status.id)
        assertThat(statusFromXml.status).isEqualTo(status.status)
    }

}
