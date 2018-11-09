package nu.westlin.kotlin.javainterop

import java.time.ZonedDateTime

/*
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

