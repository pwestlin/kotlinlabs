package nu.westlin.kotlin.javainterop

import java.time.ZonedDateTime

class PagaendeArende(
    objektidentitet: String,
    objektversion: Int = 1,
    versionGiltigFran: ZonedDateTime? = null,
    versionGiltigTill: ZonedDateTime? = null,
    val arendeidentitet: String) : Utbytesobjekt<Gemensamhetsanlaggning>(
    utbytesobjekttyp = Utbytesobjekttyp.GEMENSAMHETSANLAGGNING,
    objektidentitet = objektidentitet,
    objektversion = objektversion,
    versionGiltigFran = versionGiltigFran,
    versionGiltigTill = versionGiltigTill)
