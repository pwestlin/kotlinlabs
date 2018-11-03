package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic.utbytesobjekt

import java.time.ZonedDateTime

/*
Behöver vi builders INUTI våra domänobjekt, dvs de som ligger i src/MAIN/kotlin?
Vi behöver översätta inkommande domänobjekt (impl i java) till vår interna domänmodell.
    -Här kanske det skulle passa bättre med en converter än builder?

Vi behöver builders för TEST, med exempel-metod!

Fastighet implementeras med en intern builder.
    -Interna builders ska INTE ha en exempel-metod!
    -För test definieras en extension method exempel

Samfallighet implementeras med en test-builder med en exempel-metod som companion object.

Gemensamhetsanlaggning implementeras med en test-builder med en exempel-metod som separat klass.

PagaendeArende implementeras med en test-builder samt en top-level method exempel som separat klass.

 */

abstract class Utbytesobjekt<T : Utbytesobjekt<T>>(
    val utbytesobjekttyp: Utbytesobjekttyp,
    val objektidentitet: String,
    val objektversion: Int = 1,
    val versionGiltigFran: ZonedDateTime? = null,
    val versionGiltigTill: ZonedDateTime? = null
)

