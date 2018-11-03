package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.utbytesobjekt

import java.time.ZonedDateTime

class Gemensamhetsanlaggning(
    objektidentitet: String,
    objektversion: Int = 1,
    versionGiltigFran: ZonedDateTime? = null,
    versionGiltigTill: ZonedDateTime? = null,
    val status: String) : Utbytesobjekt<Gemensamhetsanlaggning>(
    utbytesobjekttyp = Utbytesobjekttyp.GEMENSAMHETSANLAGGNING,
    objektidentitet = objektidentitet,
    objektversion = objektversion,
    versionGiltigFran = versionGiltigFran,
    versionGiltigTill = versionGiltigTill)
