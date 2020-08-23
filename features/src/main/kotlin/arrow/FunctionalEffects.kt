package main.kotlin.arrow

import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.handleError
import arrow.unsafe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun main() {
    val program: IO<String> = IO.fx {
        val effect1 = !IO.effect { asyncValue1() }
        val effect2 = !IO.effect { asyncValue2() }
        "$effect1 $effect2"
    }
    println(program.unsafeRunSync())

    val programError: IO<String> = IO.fx {
        val effect1 = !IO.effect { asyncValue1() }
        val effect2 = !IO.effect { throw NullPointerException() }
        "$effect1 $effect2"
    }.handleError { t -> "Error Handler on program. Caused by $t" }
    println(programError.unsafeRunSync())
}

private suspend fun asyncValue2() = withContext(Dispatchers.Default) {
    "side-effect world "
}

private suspend fun asyncValue1() = withContext(Dispatchers.Default) {
    "hello pure"
}