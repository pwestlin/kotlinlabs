package nu.westlin.kotlin.learnkotlin.lessbasic.utbytesobjekt

import org.assertj.core.api.Assertions
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.ZonedDateTime

fun Fastighet.Builder.exempel() = Fastighet.apply {
    objektidentitet("foo")
    status("levande")
}

class FastighetTest {

    @Test
    fun `create Fastighet with constructor`() {
        val fastighet = Fastighet(
            objektidentitet = "foo",
            status = "levande"
        )

        with(fastighet) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.FASTIGHET)
            Assertions.assertThat(objektidentitet).isEqualTo("foo")
            Assertions.assertThat(objektversion).isEqualTo(1)
            Assertions.assertThat(versionGiltigFran).isNull()
            Assertions.assertThat(versionGiltigTill).isNull()
            Assertions.assertThat(status).isEqualTo("levande")
        }
    }

    @Test
    fun `create Fastighet with builder - apply on required`() {
        val fastighet = Fastighet.apply {
            objektidentitet("foo")
            status("levande")
        }.build()

        with(fastighet) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.FASTIGHET)
            Assertions.assertThat(objektidentitet).isEqualTo("foo")
            Assertions.assertThat(objektversion).isEqualTo(1)
            Assertions.assertThat(versionGiltigFran).isNull()
            Assertions.assertThat(versionGiltigTill).isNull()
            Assertions.assertThat(status).isEqualTo("levande")
        }
    }

    @Test
    fun `create Fastighet with builder - apply on all fields`() {
        val fran = ZonedDateTime.now().minusSeconds(3)
        val till = ZonedDateTime.now()
        val fastighet = Fastighet.apply {
            objektidentitet("bar")
            objektversion(2)
            versionGiltigFran(fran)
            versionGiltigTill(till)
            status("avregistrerad")
        }.build()

        with(fastighet) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.FASTIGHET)
            Assertions.assertThat(objektidentitet).isEqualTo("bar")
            Assertions.assertThat(objektversion).isEqualTo(2)
            Assertions.assertThat(versionGiltigFran).isEqualTo(fran)
            Assertions.assertThat(versionGiltigTill).isEqualTo(till)
            Assertions.assertThat(status).isEqualTo("avregistrerad")
        }
    }

    @Test
    fun `create Fastighet with builder - reuired field is missing`() {
        assertThrows(
            "objektidentitet krävs",
            IllegalStateException::class.java
        ) { Fastighet.build() }
    }

    @Test
    fun `Fastighet exempel`() {
        with(Fastighet.exempel().build()) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.FASTIGHET)
            Assertions.assertThat(objektidentitet).isEqualTo("foo")
            Assertions.assertThat(objektversion).isEqualTo(1)
            Assertions.assertThat(versionGiltigFran).isNull()
            Assertions.assertThat(versionGiltigTill).isNull()
            Assertions.assertThat(status).isEqualTo("levande")
        }
    }
}