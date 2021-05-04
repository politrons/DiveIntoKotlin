package main.kotlin

fun main() {
    val circuitBreaker: CircuitBreaker<Open> = CircuitBreaker(Open)
    val halfOpen: CircuitBreaker<HalfOpen> = circuitBreaker.halfOpen()
    val close: CircuitBreaker<Close> = halfOpen.close()
    val open: CircuitBreaker<Open> = close.open()
    open.halfOpen()
}

sealed class State

object Open : State()

object HalfOpen : State()

object Close : State()

class CircuitBreaker<out T : State>(val state: T)

fun CircuitBreaker<Open>.halfOpen(): CircuitBreaker<HalfOpen> {
    println("CircuitBreaker Open")
    return CircuitBreaker(HalfOpen)
}

fun CircuitBreaker<HalfOpen>.close(): CircuitBreaker<Close> {
    println("CircuitBreaker Half-open")
    return CircuitBreaker(Close)
}

fun CircuitBreaker<Close>.open(): CircuitBreaker<Open> {
    println("CircuitBreaker Close")
    return CircuitBreaker(Open)
}
