package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.utbytesobjekt

import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.time.ZonedDateTime

class GemensamhetsanlaggningTest {

    @Suppress("RedundantVisibilityModifier")
    @Rule
    @JvmField
    public var thrown = ExpectedException.none()!!


    @Test
    fun `create Gemensamhetsanlaggning with constructor`() {
        val ga = Gemensamhetsanlaggning(
            objektidentitet = "foo",
            status = "levande"
        )

        with(ga) {
            assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            assertThat(objektidentitet).isEqualTo("foo")
            assertThat(objektversion).isEqualTo(1)
            assertThat(versionGiltigFran).isNull()
            assertThat(versionGiltigTill).isNull()
            assertThat(status).isEqualTo("levande")
        }
    }

    @Test
    fun `create Gemensamhetsanlaggning with builder - apply on required`() {
        val ga = GemensamhetsanlaggningBuilder().apply {
            objektidentitet("foo")
            status("levande")
        }.build()

        with(ga) {
            assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            assertThat(objektidentitet).isEqualTo("foo")
            assertThat(objektversion).isEqualTo(1)
            assertThat(versionGiltigFran).isNull()
            assertThat(versionGiltigTill).isNull()
            assertThat(status).isEqualTo("levande")
        }
    }

    @Test
    fun `create Gemensamhetsanlaggning with builder - apply on all fields`() {
        val fran = ZonedDateTime.now().minusSeconds(3)
        val till = ZonedDateTime.now()
        val ga = GemensamhetsanlaggningBuilder().apply {
            objektidentitet("bar")
            objektversion(2)
            versionGiltigFran(fran)
            versionGiltigTill(till)
            status("avregistrerad")
        }.build()

        with(ga) {
            assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            assertThat(objektidentitet).isEqualTo("bar")
            assertThat(objektversion).isEqualTo(2)
            assertThat(versionGiltigFran).isEqualTo(fran)
            assertThat(versionGiltigTill).isEqualTo(till)
            assertThat(status).isEqualTo("avregistrerad")
        }
    }

    @Test
    fun `create Gemensamhetsanlaggning with builder - reuired field is missing`() {
        thrown.expect(IllegalStateException::class.java)
        thrown.expectMessage("objektidentitet krävs")

        GemensamhetsanlaggningBuilder().build()
    }

    @Test
    fun `Gemensamhetsanlaggning exempel`() {
        with(GemensamhetsanlaggningBuilder.exempel().build()) {
            assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            assertThat(objektidentitet).isEqualTo("foo")
            assertThat(objektversion).isEqualTo(1)
            assertThat(versionGiltigFran).isNull()
            assertThat(versionGiltigTill).isNull()
            assertThat(status).isEqualTo("levande")
        }
    }

    @Test
    fun `Gemensamhetsanlaggning exempel extension function`() {
        with(GemensamhetsanlaggningBuilder.exempel().build()) {
            assertThat(utbytesobjekttyp).isEqualTo(Utbytesobjekttyp.GEMENSAMHETSANLAGGNING)
            assertThat(objektidentitet).isEqualTo("foo")
            assertThat(objektversion).isEqualTo(1)
            assertThat(versionGiltigFran).isNull()
            assertThat(versionGiltigTill).isNull()
            assertThat(status).isEqualTo("levande")
        }
    }
}