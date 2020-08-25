package main.kotlin.arrow

import arrow.core.*
import arrow.core.extensions.fx
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.extensions.toIO
import arrow.fx.handleError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun main() {
    iOSuccessProgram()
    iOErrorProgram()
    functionalEffectProgram()
    monadsComposition()
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

private fun monadsComposition() {
    val program: IO<String> = IO.fx {
        val option1 = !IO.effect { maybeString("hello different") }
        val right1 = !IO.effect { eitherString("Monads Combining") }
        val mergeOption = Option.fx {
            val optionValue1 = !option1
            val right = Either.fx<Int, String> {
                val rightValue1 = !right1
                "$optionValue1 - $rightValue1"
            }
            right.getOrElse { "Program has no output" }
        }
        mergeOption.getOrElse { "Program has no output" }
    }
    println(program.unsafeRunSync())
}


private suspend fun asyncValue2() = withContext(Dispatchers.Default) {
    "side-effect world "
}

private suspend fun asyncValue1() = withContext(Dispatchers.Default) {
    "hello pure"
}

