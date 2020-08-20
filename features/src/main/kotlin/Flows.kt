package main.kotlin

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

/**
 * Flow is Reactive programing paradigm, just like Observable, Flux, or any other reactive monad
 * that apply Observer/Subscribe pattern.
 * Provide all typical operators for backpressure, and are cold/lazy by default.
 * To pass the emission from the stream to the subscriber, the flow ust use operator [emit]
 * In order to make it eager or subscribe, you need to use [collect]
 */
@ExperimentalCoroutinesApi
@FlowPreview
fun main() {
    runBlocking { simpleFlow() }
    runBlocking { streamFlows() }
    runBlocking { flatMapFlows() }
    runBlocking { transform() }
    runBlocking { reduce() }
    runBlocking { filter() }
    runBlocking { scan() }
    runBlocking { bulkHeadPattern() }
    runBlocking { zipFlows() }
    runBlocking { errorHandler() }
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
    (1..5)
        .asFlow()
        .map { number -> number * 10 }
        .collect { result -> println(result) }
}

/**
 * As we mention before, since Flow are monads, we can compose Flows using [flatMapMerge] which it will compose
 * a new flow with the previous one sequentially.
 */
@FlowPreview
private suspend fun flatMapFlows() {
    listOf("hello", "composition", "flow", "world")
        .asFlow()
        .flatMapConcat { number -> flow { emit(number.toUpperCase()) } }
        .collect { result -> println(result) }
}

/**
 * Transform operator allow receive the data, and emit as much data as we want using [emit] operator.
 */
private suspend fun transform() {
    listOf("hello", "transform", "flow", "world")
        .asFlow()
        .transform { value ->
            emit(value)
            emit("$value _")
            emit("$value __")
        }
        .collect { result -> println(result) }
}

/**
 * Using reduce operator it will pass execute a function where it will receive the accumulator value, and
 * the next element of the array. Just like fold, but here the accumulator type is always the same than element
 * you have in the iterator
 */
private suspend fun reduce() {
    val sum = listOf(1, 2, 3, 4, 5)
        .asFlow()
        .reduce { acc, next -> acc + next }
    println(sum)
}

/**
 * Regular filter operators like in all reactive streams frameworks.
 */
private suspend fun filter() {
    listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        .asFlow()
        .filter { value -> value > 3 }
        .takeWhile { value -> value < 7 }
        .collect { number -> println(number) }
}

/**
 * [Scan] operator is the [fold] in the reactive stream world. It works exactly in the same way, you declare
 * as first argument the accumulator type, and the second argument is a function where you receive the
 * accumulator type defined previously and the next element of the iterator
 */
@ExperimentalCoroutinesApi
private suspend fun scan() {
    val numbers =
        listOf(1, 2, 3, 4, 5)
            .asFlow()
            .scan(emptyList<Int>()) { acc, value -> acc + value }
            .toList()
            .flatten()
    println(numbers)
}

/**
 * Using [flowOn] operator, just like [subscribeOn] of Rx we are able to configure in which dispatcher we want to
 * execute the stream, setting a maximum resources(Threads) that the stream can consume.
 */
private suspend fun bulkHeadPattern() {
    listOf("hello", "dispatcher", "flow", "world")
        .asFlow()
        .onEach { result -> println("Element $result running in thread ${Thread.currentThread().name}") }
        .flowOn(context = newFixedThreadPoolContext(10, "MyLightweightThread"))
        .collect { result -> println("Final result $result in thread ${Thread.currentThread().name}") }
}

/**
 * [zip] is another standard reactive operator to run two streams in parallel and then merge together once both
 * have finish their tasks
 */
private suspend fun zipFlows() {
    val flow1: Flow<String> = listOf("hello", "zip")
        .asFlow()
    val flow2: Flow<String> = listOf("flow", "world")
        .asFlow()
    flow1.zip(flow2) { value1, value2 -> "$value1 - $value2" }
        .map { value -> value.toUpperCase() }
        .collect { result -> println(result) }
}

/**
 * Error handler in Stream to get all possible throwable, and allow you to return a last element before the stream is stopped.
 * Using inside the catch the [emitAll] it's allow you to return last emission in the stream.
 * Pretty much the same than [onErrorResumeNext] of Rx
 */
private suspend fun errorHandler() {
    listOf("1", "2", "flow", "3")
        .asFlow()
        .map { result -> result.toInt() }
        .catch { t ->
            println("Error in stream. Caused by $t")
            emitAll(flow{emit(666)})
        }
        .collect { result -> println(result) }
}