package nu.westlin.kotlin.learnkotlin.lessbasic.utbytesobjekt

import org.assertj.core.api.Assertions
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.ZonedDateTime

class PagaendeArendeTest {

    @Test
    fun `create PagaendeArende with constructor`() {
        val fastighet = PagaendeArende(
            objektidentitet = "foo",
            arendeidentitet = "abc123"
        )

        with(fastighet) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            Assertions.assertThat(objektidentitet).isEqualTo("foo")
            Assertions.assertThat(objektversion).isEqualTo(1)
            Assertions.assertThat(versionGiltigFran).isNull()
            Assertions.assertThat(versionGiltigTill).isNull()
            Assertions.assertThat(arendeidentitet).isEqualTo("abc123")
        }
    }

    @Test
    fun `create PagaendeArende with builder - apply on required`() {
        val fastighet = PagaendeArendeBuilder().apply {
            objektidentitet("foo")
            arendeidentitet("abc123")
        }.build()

        with(fastighet) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            Assertions.assertThat(objektidentitet).isEqualTo("foo")
            Assertions.assertThat(objektversion).isEqualTo(1)
            Assertions.assertThat(versionGiltigFran).isNull()
            Assertions.assertThat(versionGiltigTill).isNull()
            Assertions.assertThat(arendeidentitet).isEqualTo("abc123")
        }
    }

    @Test
    fun `create PagaendeArende with builder - apply on all fields`() {
        val fran = ZonedDateTime.now().minusSeconds(3)
        val till = ZonedDateTime.now()
        val fastighet = PagaendeArendeBuilder().apply {
            objektidentitet("bar")
            objektversion(2)
            versionGiltigFran(fran)
            versionGiltigTill(till)
            arendeidentitet("xyz789")
        }.build()

        with(fastighet) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            Assertions.assertThat(objektidentitet).isEqualTo("bar")
            Assertions.assertThat(objektversion).isEqualTo(2)
            Assertions.assertThat(versionGiltigFran).isEqualTo(fran)
            Assertions.assertThat(versionGiltigTill).isEqualTo(till)
            Assertions.assertThat(arendeidentitet).isEqualTo("xyz789")
        }
    }

    @Test
    fun `create PagaendeArende with builder - reuired field is missing`() {
        assertThrows("objektidentitet krävs", IllegalStateException::class.java) { PagaendeArendeBuilder().build() }
    }

    @Test
    fun `PagaendeArende exempel`() {
        with(exempel().build()) {
            Assertions.assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            Assertions.assertThat(objektidentitet).isEqualTo("foo")
            Assertions.assertThat(objektversion).isEqualTo(1)
            Assertions.assertThat(versionGiltigFran).isNull()
            Assertions.assertThat(versionGiltigTill).isNull()
            Assertions.assertThat(arendeidentitet).isEqualTo("abc123")
        }
    }
}