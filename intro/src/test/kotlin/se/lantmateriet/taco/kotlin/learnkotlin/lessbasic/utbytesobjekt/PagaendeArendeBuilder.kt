package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.utbytesobjekt

import java.time.ZonedDateTime


fun exempel() = PagaendeArendeBuilder().apply {
    objektidentitet("foo")
    objektversion(1)
    versionGiltigFran(null)
    versionGiltigTill(null)
    arendeidentitet("abc123")
}


class PagaendeArendeBuilder {
    private var objektidentitet: String? = null
    private var objektversion: Int = 1
    private var versionGiltigFran: ZonedDateTime? = null
    private var versionGiltigTill: ZonedDateTime? = null
    private var arendeidentitet: String? = null

    fun objektidentitet(objektidentitet: String) = apply { this.objektidentitet = objektidentitet }
    fun objektversion(objektversion: Int) = apply { this.objektversion = objektversion }
    fun versionGiltigFran(versionGiltigFran: ZonedDateTime?) = apply { this.versionGiltigFran = versionGiltigFran }
    fun versionGiltigTill(versionGiltigTill: ZonedDateTime?) = apply { this.versionGiltigTill = versionGiltigTill }
    fun arendeidentitet(arendeidentitet: String) = apply { this.arendeidentitet = arendeidentitet }

    fun build(): PagaendeArende {
        return PagaendeArende(
            objektidentitet = objektidentitet ?: throw IllegalStateException("objektidentitet krävs"),
            objektversion = objektversion,
            versionGiltigFran = versionGiltigFran,
            versionGiltigTill = versionGiltigTill,
            arendeidentitet = arendeidentitet ?: throw IllegalStateException("arendeidentitet krävs")
        )
    }
}