package main.kotlin

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() {
    simpleFlow()
    mapFlows()
    flatMapFlows()
}

private fun simpleFlow() {
    runBlocking<Unit> {
        val flow = flow {
            emit("hello stream")
        }.map { value -> value.toUpperCase() }
        flow.collect { res -> println(res) }
    }
}

private fun mapFlows() {
    runBlocking<Unit> {
        (1..5).asFlow()
            .map { number -> number * 10 }
            .collect { result -> println(result) }
    }
}

private fun flatMapFlows() {
    runBlocking {
        listOf("hello","composition","flow","world").asFlow()
            .flatMapConcat { number -> flow { emit(number.toUpperCase()) } }
            .collect { result -> println(result) }
    }
}