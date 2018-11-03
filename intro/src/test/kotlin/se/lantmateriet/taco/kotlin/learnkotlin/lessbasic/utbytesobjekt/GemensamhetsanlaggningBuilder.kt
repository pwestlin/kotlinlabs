package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.utbytesobjekt

import java.time.ZonedDateTime

class GemensamhetsanlaggningBuilder {
    private var objektidentitet: String? = null
    private var objektversion: Int = 1
    private var versionGiltigFran: ZonedDateTime? = null
    private var versionGiltigTill: ZonedDateTime? = null
    private var status: String? = null

    fun objektidentitet(objektidentitet: String) = apply { this.objektidentitet = objektidentitet }
    fun objektversion(objektversion: Int) = apply { this.objektversion = objektversion }
    fun versionGiltigFran(versionGiltigFran: ZonedDateTime?) = apply { this.versionGiltigFran = versionGiltigFran }
    fun versionGiltigTill(versionGiltigTill: ZonedDateTime?) = apply { this.versionGiltigTill = versionGiltigTill }
    fun status(status: String) = apply { this.status = status }

    fun build(): Gemensamhetsanlaggning {
        return Gemensamhetsanlaggning(
            objektidentitet = objektidentitet ?: throw IllegalStateException("objektidentitet krävs"),
            objektversion = objektversion,
            versionGiltigFran = versionGiltigFran,
            versionGiltigTill = versionGiltigTill,
            status = status ?: throw IllegalStateException("status krävs")
        )
    }

    companion object {
        fun exempel() = GemensamhetsanlaggningBuilder().apply {
            objektidentitet("foo")
            objektversion(1)
            versionGiltigFran(null)
            versionGiltigTill(null)
            status("levande")
        }
    }
}