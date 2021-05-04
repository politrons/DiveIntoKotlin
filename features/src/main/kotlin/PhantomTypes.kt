package main.kotlin

/**
 * [Phantom types] is just a pattern where using [Generic types] and [Extension function],
 * we can create some function that are only available for a specific class with a specific generic type.
 * Having this, we can change from one state to another, making available new function and making disable others
 * that were available before.
 */
fun main() {
    val circuitBreaker: CircuitBreaker<Open> = CircuitBreaker(Open)
    val halfOpen: CircuitBreaker<HalfOpen> = circuitBreaker.halfOpen()
    val close: CircuitBreaker<Close> = halfOpen.close()
    val open: CircuitBreaker<Open> = close.open()
    open.halfOpen()
}

/**
 * State types of Circuit Breaker
 * ------------------------------
 */
sealed class State

object Open : State()

object HalfOpen : State()

object Close : State()

/**
 * Class with a covariant type of [State] that define the state of the [CircuitBreaker]
 */
class CircuitBreaker<out T : State>(val state: T)

/**
 * Using extension functions we only allow the function [halfOpen] for the instance
 * [CircuitBreaker] with State type [Open]
 */
fun CircuitBreaker<Open>.halfOpen(): CircuitBreaker<HalfOpen> {
    println("CircuitBreaker Open to Half-Open")
    return CircuitBreaker(HalfOpen)
}

@JvmName("openOpen")
fun CircuitBreaker<Open>.open(): CircuitBreaker<Open> {
    println("CircuitBreaker Open to Open")
    return CircuitBreaker(Open)
}

/**
 * Using extension functions we only allow the function [close] for the instance
 * [CircuitBreaker] with State type [HalfOpen]
 */
fun CircuitBreaker<HalfOpen>.close(): CircuitBreaker<Close> {
    println("CircuitBreaker Half-open to Close")
    return CircuitBreaker(Close)
}

@JvmName("openHalfOpen")
fun CircuitBreaker<HalfOpen>.open(): CircuitBreaker<Open> {
    println("CircuitBreaker Half-open")
    return CircuitBreaker(Open)
}

/**
 * Using extension functions we only allow the function [open] for the instance
 * [CircuitBreaker] with State type [Close]
 */
fun CircuitBreaker<Close>.open(): CircuitBreaker<Open> {
    println("CircuitBreaker Close to Open")
    return CircuitBreaker(Open)
}

