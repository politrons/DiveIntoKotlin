package main.kotlin.arrow

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.right
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.toIO
import arrow.fx.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun main() {
    iOSuccessProgram()
    iOErrorProgram()
    functionalEffectProgram()
}

private fun iOSuccessProgram() {
    val program: IO<String> = IO.fx {
        val effect1 = !IO.effect { asyncValue1() }
        val effect2 = !IO.effect { asyncValue2() }
        "$effect1 $effect2"
    }
    println(program.unsafeRunSync())
}

private fun iOErrorProgram() {
    val programError: IO<String> = IO.fx {
        val effect1 = !IO.effect { asyncValue1() }
        val effect2 = !IO.effect { throw NullPointerException() }
        "$effect1 $effect2"
    }.handleError { t -> "Error Handler on program. Caused by $t" }
    println(programError.unsafeRunSync())
}

fun functionalEffectProgram() {
    val functionalEffectsProgram: IO<String> = IO.fx {
        val optionValue = !IO.effect { maybeString("hello pure").getOrElse { throw IllegalStateException() } }
        val eitherValue = when (val eitherValue =
            !eitherString("functional effect world").right().toIO()) {
            is Either.Right -> eitherValue.b
            is Either.Left -> throw IllegalStateException()
        }
        "$optionValue - $eitherValue"
    }.handleError { t -> "Error Handler on functional effect program. Caused by $t" }
    println(functionalEffectsProgram.unsafeRunSync())
}

private suspend fun asyncValue2() = withContext(Dispatchers.Default) {
    "side-effect world "
}

private suspend fun asyncValue1() = withContext(Dispatchers.Default) {
    "hello pure"
}

