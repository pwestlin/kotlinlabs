package nu.westlin.kotlin.fastighet

import org.junit.Test

class FastighetServiceTests {

    @Test
    fun `a a`() {
        FastighetService().doSomething(Kommun(Objektidentitet.randomObjektidentitet(), "Gävle"))
    }
}