@file:Suppress("TestFunctionName", "ObjectPropertyName")

package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

private const val tidAttUtföraArbete: Long = 500

private class Mekaniker {
    fun utför(arbete: () -> Unit) {
        arbete()
    }
}

private val lyftFram = {
    log("Lyfter bilen fram")
    Thread.sleep(tidAttUtföraArbete)
    log("Lyft bilen fram")
}

private val sänkFram = {
    log("Sänker bilen fram")
    Thread.sleep(tidAttUtföraArbete)
    log("Sänkt bilen fram")
}

private val lyftBak = {
    log("Lyfter bilen fram")
    Thread.sleep(tidAttUtföraArbete)
    log("Lyft bilen fram")
}

private val sänkBak = {
    log("Sänker bilen bak")
    Thread.sleep(tidAttUtföraArbete)
    log("Sänkt bilen bak")
}

private val skruvaLossDäck = { däck: String ->
    log("Skruvar loss ${däck}däck")
    Thread.sleep(tidAttUtföraArbete)
    log("Skruvat loss ${däck}däck")
}

private val skruvaFastDäck = { däck: String ->
    log("Skruvar fast ${däck}däck")
    Thread.sleep(tidAttUtföraArbete)
    log("Skruvat fast ${däck}däck")
}

private val lyftBortDäck = { däck: String ->
    log("Lyfter bort ${däck}däck")
    Thread.sleep(tidAttUtföraArbete)
    log("Lyft bort ${däck}däck")
}

private val lyftDitDäck = { däck: String ->
    log("Lyfter dit ${däck}däck")
    Thread.sleep(tidAttUtföraArbete)
    log("Lyft dit ${däck}däck")
}

private val bytDäck = { däck: String ->
    TODO("Impl")
}

internal class F1TeamTest {
    private val främreLyftare = Mekaniker()
    private val bakreLyftare = Mekaniker()

    private val vänsterBakdäckSkruvare = Mekaniker()
    private val vänsterBakdäckBortlyftare = Mekaniker()
    private val vänsterBakdäckDitlyftare = Mekaniker()

    private val högerBakdäckSkruvare = Mekaniker()
    private val högerBakdäckBortlyftare = Mekaniker()
    private val högerBakdäckDitlyftare = Mekaniker()

    private val vänsterFramdäckSkruvare = Mekaniker()
    private val vänsterFramdäckBortlyftare = Mekaniker()
    private val vänsterFramdäckDitlyftare = Mekaniker()

    private val högerFramdäckSkruvare = Mekaniker()
    private val högerFramdäckBortlyftare = Mekaniker()
    private val högerFramdäckDitlyftare = Mekaniker()

    @Test
    fun `byt däck`() {
        val exekveringstid = measureTimeMillis {
            // lyft bilen
            främreLyftare.utför { lyftFram }
            bakreLyftare.utför { lyftBak }

            // vänster bak
            vänsterBakdäckBortlyftare.utför { lyftBortDäck("vänster bak") }
            vänsterBakdäckSkruvare.utför { skruvaLossDäck("vänster bak") }
            vänsterBakdäckDitlyftare.utför { lyftDitDäck("vänster bak") }
            vänsterBakdäckSkruvare.utför { skruvaFastDäck("vänster bak") }

            // höger bak
            högerBakdäckBortlyftare.utför { lyftBortDäck("höger bak") }
            högerBakdäckSkruvare.utför { skruvaLossDäck("höger bak") }
            högerBakdäckDitlyftare.utför { lyftDitDäck("höger bak") }
            högerBakdäckSkruvare.utför { skruvaFastDäck("höger bak") }

            // vänster fram
            vänsterFramdäckBortlyftare.utför { lyftBortDäck("vänster fram") }
            vänsterFramdäckSkruvare.utför { skruvaLossDäck("vänster fram") }
            vänsterFramdäckDitlyftare.utför { lyftDitDäck("vänster fram") }
            vänsterFramdäckSkruvare.utför { skruvaFastDäck("vänster fram") }

            // höger Fram
            högerFramdäckBortlyftare.utför { lyftBortDäck("höger fram") }
            högerFramdäckSkruvare.utför { skruvaLossDäck("höger fram") }
            högerFramdäckDitlyftare.utför { lyftDitDäck("höger fram") }
            högerFramdäckSkruvare.utför { skruvaFastDäck("höger fram") }

            // Sänk bilen
            främreLyftare.utför { sänkFram }
            bakreLyftare.utför { lyftBak }

            // När bilen stannat -> Börja

            // Lyft fram
            // Lyft bak

            // När alla däckskruvare signalerat att de är klara -> Sänk bilen fram och bak

            // Skicka iväg bilen
        }

        log("Exekveringstid = $exekveringstid")
    }
}