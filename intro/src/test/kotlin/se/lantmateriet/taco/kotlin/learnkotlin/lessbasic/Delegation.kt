package se.lantmateriet.taco.kotlin.learnkotlin.lessbasic

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

interface Engine {
    fun power(): Int
}

interface FossileEngine : Engine {
    fun tankVolume(): Int
}

interface ElectricalEngine : Engine {
    fun batteryCapacity(): Int
}

class PetrolEngine(private val power: Int, private val tankVolume: Int) : FossileEngine {
    override fun power() = power

    override fun tankVolume() = tankVolume
}

class BatteryEngine(private val power: Int, private val batteryCapacity: Int) : ElectricalEngine {
    override fun power() = power

    override fun batteryCapacity() = batteryCapacity
}

interface Vehicle {
    fun noSeats(): Int
}

class OpenSeater : Vehicle {
    override fun noSeats() = 1
}

class RoadCar : Vehicle {
    override fun noSeats() = 5
}

class HybridCar(fossilePower: Int, tankVolume: Int, batteryPower: Int, batteryCapacity: Int)
    : Vehicle by RoadCar(), FossileEngine by PetrolEngine(fossilePower, tankVolume), ElectricalEngine by BatteryEngine(batteryPower, batteryCapacity
) {
    private val power = fossilePower + batteryPower

    override fun power() = power
}

class FormulaOne : Vehicle by OpenSeater(), FossileEngine by PetrolEngine(900, 100)

class FormulaE : Vehicle by OpenSeater(), ElectricalEngine by BatteryEngine(500, 10000)


class DelegationTest {

    @Test
    fun `create a Formula 1`() {
        with(FormulaOne()) {
            assertThat(power()).isEqualTo(900)
            assertThat(tankVolume()).isEqualTo(100)
            assertThat(noSeats()).isEqualTo(1)
        }
    }

    @Test
    fun `create a Formula E`() {
        with(FormulaE()) {
            assertThat(power()).isEqualTo(500)
            assertThat(batteryCapacity()).isEqualTo(10000)
            assertThat(noSeats()).isEqualTo(1)
        }
    }

    @Test
    fun `create a hybric car`() {
        with(HybridCar(120, 80, 40, 5000)) {
            assertThat(power()).isEqualTo(160)
            assertThat(tankVolume()).isEqualTo(80)
            assertThat(batteryCapacity()).isEqualTo(5000)
            assertThat(noSeats()).isEqualTo(5)
        }
    }
}