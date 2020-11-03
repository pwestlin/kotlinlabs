package nu.westlin.kotlin.learnkotlin.lessbasic.gofpatterns

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

/**
 * [State pattern](https://en.wikipedia.org/wiki/State_pattern)
 */
class StatePattern {
    // TODO petves: Refakt with lambdas?

    interface CalculatorState {
        fun calculate(first: Int, second: Int): Int
    }

    class PlusState: CalculatorState {
        override fun calculate(first: Int, second: Int): Int = first + second
    }

    class MinusState: CalculatorState {
        override fun calculate(first: Int, second: Int): Int = first - second
    }

    // TODO petves: Delegation?
    class CalculatorContext(var state: CalculatorState) {
        fun calculate(first: Int, second: Int): Int = state.calculate(first, second)
    }

    @Test
    fun `calculate stuff`() {
        val calculator = CalculatorContext(PlusState())
        Assertions.assertThat(calculator.calculate(1, 2)).isEqualTo(3)

        calculator.state = MinusState()
        Assertions.assertThat(calculator.calculate(1, 2)).isEqualTo(-1)
    }
}