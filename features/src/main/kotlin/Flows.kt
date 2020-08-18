package main.kotlin

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking<Unit> {
        val flow = flow {
            emit("hello stream")
        }.map { value -> value.toUpperCase() }

        flow.collect { res -> println(res) }

    }
}