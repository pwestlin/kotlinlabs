package nu.westlin.kotlin.javainterop

import java.time.ZonedDateTime

sealed class Registerenhet<T : Registerenhet<T>>(
    utbytesobjekttyp: Utbytesobjekttyp,
    objektidentitet: String,
    objektversion: Int = 1,
    versionGiltigFran: ZonedDateTime? = null,
    versionGiltigTill: ZonedDateTime? = null,
    val status: String) : Utbytesobjekt<Registerenhet<T>>(
    utbytesobjekttyp = utbytesobjekttyp,
    objektidentitet = objektidentitet,
    objektversion = objektversion,
    versionGiltigFran = versionGiltigFran,
    versionGiltigTill = versionGiltigTill)

class Fastighet(
    objektidentitet: String,
    objektversion: Int = 1,
    versionGiltigFran: ZonedDateTime? = null,
    versionGiltigTill: ZonedDateTime? = null,
    status: String) : Registerenhet<Fastighet>(
    utbytesobjekttyp = Utbytesobjekttyp.FASTIGHET,
    objektidentitet = objektidentitet,
    objektversion = objektversion,
    versionGiltigFran = versionGiltigFran,
    versionGiltigTill = versionGiltigTill,
    status = status) {

    companion object Builder {
        private var objektidentitet: String? = null
        private var objektversion: Int = 1
        private var versionGiltigFran: ZonedDateTime? = null
        private var versionGiltigTill: ZonedDateTime? = null
        private var status: String? = null

        fun objektidentitet(objektidentitet: String) = apply { Builder.objektidentitet = objektidentitet }
        fun objektversion(objektversion: Int) = apply { Builder.objektversion = objektversion }
        fun versionGiltigFran(versionGiltigFran: ZonedDateTime?) = apply { Builder.versionGiltigFran = versionGiltigFran }
        fun versionGiltigTill(versionGiltigTill: ZonedDateTime?) = apply { Builder.versionGiltigTill = versionGiltigTill }
        fun status(status: String) = apply { Builder.status = status }

        fun build(): Fastighet {
            return Fastighet(
                objektidentitet = objektidentitet ?: throw IllegalStateException("objektidentitet krävs"),
                objektversion = objektversion,
                versionGiltigFran = versionGiltigFran,
                versionGiltigTill = versionGiltigTill,
                status = status ?: throw IllegalStateException("status krävs")
            )
        }
    }
}

class Samfallighet(
    objektidentitet: String,
    objektversion: Int = 1,
    versionGiltigFran: ZonedDateTime? = null,
    versionGiltigTill: ZonedDateTime? = null,
    status: String) : Registerenhet<Samfallighet>(
    utbytesobjekttyp = Utbytesobjekttyp.SAMFALLIGHET,
    objektidentitet = objektidentitet,
    objektversion = objektversion,
    versionGiltigFran = versionGiltigFran,
    versionGiltigTill = versionGiltigTill,
    status = status)