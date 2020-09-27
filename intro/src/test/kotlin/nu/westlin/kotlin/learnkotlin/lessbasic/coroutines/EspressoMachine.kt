@file:Suppress("EXPERIMENTAL_API_USAGE")

// Copied from https://proandroiddev.com/kotlin-coroutines-channels-csp-android-db441400965f

package nu.westlin.kotlin.learnkotlin.lessbasic.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import kotlin.system.measureTimeMillis

sealed class CoffeeBean {
    abstract fun price(): Float

    object Premium : CoffeeBean() {
        override fun price() = 1.00f
        override fun toString() = "premium"
    }

    object Regular : CoffeeBean() {
        override fun price() = 0.00f
        override fun toString() = "regular"
    }

    object Decaf : CoffeeBean() {
        override fun price() = 0.50f
        override fun toString() = "decaf"
    }

    data class GroundBeans(val coffeeBean: CoffeeBean) : CoffeeBean() {
        override fun price() = 0.00f
        override fun toString() = "ground $coffeeBean"
    }
}

sealed class Milk {
    abstract fun price(): Float

    object Whole : Milk() {
        override fun price() = 0.00f

        override fun toString() = "whole milk"
    }

    object NonFat : Milk() {
        override fun price() = 0.00f

        override fun toString() = "non-fat milk"
    }

    object Breve : Milk() {
        override fun price() = 1.00f

        override fun toString() = "breve"
    }

    data class SteamedMilk(val milk: Milk) : Milk() {
        override fun price() = 0.00f

        override fun toString() = "steamed milk"
    }
}

data class Espresso(val beans: CoffeeBean.GroundBeans)

sealed class Menu {
    abstract fun price(): Float

    abstract fun beans(): CoffeeBean

    abstract fun milk(): Milk

    data class Cappuccino(val beans: CoffeeBean, val milk: Milk) : Menu() {
        override fun price() = 3.50f + beans.price() + milk.price()

        override fun beans() = beans

        override fun milk() = milk

        override fun toString() = "cappuccino: beans=$beans milk=$milk price=$${price().format(2)}"
    }
}

sealed class Beverage {
    data class Cappuccino(val order: Menu.Cappuccino, val espressoShot: Espresso, val steamedMilk: Milk.SteamedMilk) : Beverage()
}

class EspressoMachine(scope: CoroutineScope) : CoroutineScope by scope {
    data class PullEspressoShotRequest(val groundBeans: CoffeeBean.GroundBeans, val espressoChan: SendChannel<Espresso>)

    data class SteamMilkRequest(val milk: Milk, val steamMilkChan: SendChannel<Milk.SteamedMilk>)

    private val portafilterOne: SendChannel<PullEspressoShotRequest> = actor {
        consumeEach {
            log("Pulling espresso shot on portafilter one")
            delay(20)
            it.espressoChan.send(Espresso(it.groundBeans))
            it.espressoChan.close()
        }
    }

    private val portafilterTwo: SendChannel<PullEspressoShotRequest> = actor {
        consumeEach {
            log("Pulling espresso shot on portafilter two")
            delay(20)
            it.espressoChan.send(Espresso(it.groundBeans))
            it.espressoChan.close()
        }
    }

    private val steamWandOne: SendChannel<SteamMilkRequest> = actor {
        consumeEach {
            log("Steaming milk with steam wand one")
            delay(10)
            it.steamMilkChan.send(Milk.SteamedMilk(it.milk))
            it.steamMilkChan.close()
        }
    }

    private val steamWandTwo: SendChannel<SteamMilkRequest> = actor {
        consumeEach {
            log("Steaming milk with steam wand two")
            delay(10)
            it.steamMilkChan.send(Milk.SteamedMilk(it.milk))
            it.steamMilkChan.close()
        }
    }

    suspend fun pullEspressoShot(groundBeans: CoffeeBean.GroundBeans) = select<Espresso> {
        val channel = Channel<Espresso>()
        val req = PullEspressoShotRequest(groundBeans, channel)
        portafilterOne.onSend(req) {
            channel.receive()
        }
        portafilterTwo.onSend(req) {
            channel.receive()
        }
    }

    suspend fun steamMilk(milk: Milk) = select<Milk.SteamedMilk> {
        val chan = Channel<Milk.SteamedMilk>()
        val req = SteamMilkRequest(milk, chan)
        steamWandOne.onSend(req) {
            chan.receive()
        }
        steamWandTwo.onSend(req) {
            chan.receive()
        }
    }

    fun shutdown() {
        portafilterOne.close()
        portafilterTwo.close()
        steamWandOne.close()
        steamWandTwo.close()
    }
}


// Introduce the Espresso Machine. This is a shared resource for both the baristas to access to pull
// espresso shots from and steam the milk. This also means we can perform the two tasks asynchronously.
fun main() = runBlocking {
    val orders = listOf(Menu.Cappuccino(CoffeeBean.Regular, Milk.Whole),
        Menu.Cappuccino(CoffeeBean.Premium, Milk.Breve),
        Menu.Cappuccino(CoffeeBean.Regular, Milk.NonFat),
        Menu.Cappuccino(CoffeeBean.Decaf, Milk.Whole),
        Menu.Cappuccino(CoffeeBean.Regular, Milk.NonFat),
        Menu.Cappuccino(CoffeeBean.Decaf, Milk.NonFat))
    log(orders)

    val espressoMachine = EspressoMachine(this)
    val ordersChannel = produce(CoroutineName("cashier")) {
        for (o in orders) {
            send(o)
        }
    }

    val t = measureTimeMillis {
        coroutineScope {
            launch(CoroutineName("barista-1")) { makeCoffee(ordersChannel, espressoMachine) }
            launch(CoroutineName("barista-2")) { makeCoffee(ordersChannel, espressoMachine) }
        }
    }
    // we need to shutdown the machine which terminates the actors launched internally
    espressoMachine.shutdown()
    println("Execution time: $t ms")
}

private suspend fun makeCoffee(orders: ReceiveChannel<Menu>, espressoMachine: EspressoMachine) {
    for (o in orders) {
        log("Processing order: $o")
        when (o) {
            is Menu.Cappuccino -> {
                val groundBeans = grindCoffeeBeans(o.beans())
                // Without async, the pull espresso shot operation would cause this function to suspend execution
                // and then execute the next line after it completes. Using async here and with steam milk means
                // we can do these two operations asynchronously. We can then call '.await' on the these two objects
                // which will suspend execution until both of the operations complete.
                coroutineScope {
                    val espressoShotDeferred = async { espressoMachine.pullEspressoShot(groundBeans) }
                    val steamedMilkDeferred = async { espressoMachine.steamMilk(o.milk()) }
                    val cappuccino = makeCappuccino(o, espressoShotDeferred.await(), steamedMilkDeferred.await())
                    log("Serve: $cappuccino")
                }
            }
        }
    }
}

private suspend fun grindCoffeeBeans(beans: CoffeeBean): CoffeeBean.GroundBeans {
    log("Grinding beans")
    delay(30)
    return CoffeeBean.GroundBeans(beans)
}

private suspend fun makeCappuccino(order: Menu.Cappuccino, espressoShot: Espresso, milk: Milk.SteamedMilk): Beverage.Cappuccino {
    log("Combining ingredients")
    delay(5)
    return Beverage.Cappuccino(order, espressoShot, milk)
}

fun log(v: Any) = println("[${Thread.currentThread().name}] $v")

fun Float.format(digits: Int): String = java.lang.String.format("%.${digits}f", this)