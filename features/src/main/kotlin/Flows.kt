package main.kotlin

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * Flow is Reactive programing paradigm, just like Observable, Flux, or any other reactive monad
 * that apply Observer/Subscribe pattern.
 * Provide all typical operators for backpressure, and are cold/lazy by default.
 * To pass the emission from the stream to the subscriber, the flow ust use operator [emit]
 * In order to make it eager or subscribe, you need to use [collect]
 */
@FlowPreview
fun main() {
    runBlocking { simpleFlow() }
    runBlocking { streamFlows() }
    runBlocking { flatMapFlows() }
}

/**
 * We can create a flow, just using [flow] keyword follow by bracket where we set the emission of the
 * element in the stream.
 * As we mention before, to unblock the stream and flow the data, we need to use [emit] operator.
 * which it will send the data to the [collect] operator which it has a function that expect that data.
 */
private suspend fun simpleFlow() {
    val flow: Flow<String> = flow {
        emit("hello stream")
    }.map { value -> value.toUpperCase() }
    flow.collect { res -> println(res) }

}

/**
 * We can also create a flow from a collection or any other type that guarantee the continuation of the stream,
 * so the flow will emit more than one element.
 * As monads [map] operator to transform the data emitted is allowed.
 */
private suspend fun streamFlows() {
    (1..5).asFlow()
        .map { number -> number * 10 }
        .collect { result -> println(result) }
}

/**
 * As we mention before, since Flow are monads, we can compose Flows usoing [flatMapMerge] which it will compose
 * a new flow with the previous one sequentially.
 */
@FlowPreview
private suspend fun flatMapFlows() {
    listOf("hello", "composition", "flow", "world").asFlow()
        .flatMapMerge { number -> flow { emit(number.toUpperCase()) } }
        .collect { result -> println(result) }
}