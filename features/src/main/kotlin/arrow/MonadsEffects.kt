@file:Suppress("NAME_SHADOWING")

package main.kotlin.arrow

import arrow.core.*
import arrow.core.extensions.fx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.random.Random

/**
 * Arrow core library brings to Kotlin Functional programing effects, to control side-effects using different
 * monads for different needs of side-effect your program it might suffer.
 */
fun main() {
    optionMonad()
    tryMonad()
    eitherMonad()
    optionFx()
    eitherFx()
    customMonad()
    runBlocking { customCoroutine() }
}

/**
 * Arrow extension brings Option monad to Kotlin, allowing us to control elements that might be null.
 * It allows two values Some and None just like Scala
 */
private fun optionMonad() {
    fun optionWhen(maybeString: Option<String>): String {
        return when (maybeString) {
            is Some -> maybeString.t
            is None -> "Not value found"
        }
    }

    val maybeString: Option<String> = Option("hello side effect of null")
        .map { value -> value.toUpperCase() }
        .flatMap { value -> Option("$value !!!") }

    println(optionWhen(maybeString))
    println(optionWhen(None))

}

/**
 * Arrow extension brings Try monad to Kotlin, allowing us to control side-effect of a value or Throwable.
 * It allows two values Success and Failure in case of Throwable.
 * Seems it has been deprecated in favor of Either.
 */
private fun tryMonad() {
    fun tryWhen(tryString: Try<String>) {
        when (tryString) {
            is Try.Success<String> -> println(tryString.value)
            is Try.Failure -> println(tryString.exception)
        }
    }

    val tryString: Try<String> =
        Try.just("Hello side-effect of Throwable")
            .map { value -> value.toUpperCase() }
            .flatMap { value -> Try.just("$value!!!") }
    tryWhen(tryString)
    tryWhen(Failure(NullPointerException()))

}

/**
 * Arrow extension brings Either monad to Kotlin, allowing us to control side-effect of a right value or left error channel.
 * It allows two values Left and Right.
 */
private fun eitherMonad() {
    fun eitherWhen(either: Either<Int, String>): Any {
        return when (either) {
            is Either.Right<String> -> either.b
            is Either.Left<Int> -> either.a
        }
    }

    val right: Either<Int, String> = Right("Hello side-effect of error channel")
        .map { value -> value.toUpperCase() }
    val left: Either<Int, String> = Left(1981)
        .mapLeft { value -> value * 10 }
    println(eitherWhen(right))
    println(eitherWhen(left))
}

/**
 * Arrow also provide the feature of [for-comprehension] to compose in a sugar syntax style monads of same type.
 * Here we use [Option.fx] and inside the curly braces we can define with [!] all Option monads, and the values
 * are extracted to allow composition of types, just like for-comprehension does.
 * It will Always return Option type.
 * In case any of the Option are None the whole composition stop and it will return None
 */
private fun optionFx() {
    val maybeValue: Option<String> = Option.fx {
        val option1 = !Some("hello ")
        val option2 = !Some("option ")
        val option3 = !Some("world ")
        val option4 = !Some("sequentially")
        option1 + option2 + option3 + option4
    }
    println(maybeValue)

    val noneValue: Option<String> = Option.fx {
        val option1 = !maybeString("hello ")
        val option2 = !maybeString("option ")
        val option3 = !maybeString("world")
        val option4 = !maybeString("sequentially ")
        option1 + option2 + option3 + option4
    }
    println(noneValue)
}

/**
 * Here we use [Either.fx] and inside the curly braces we can define with [!] all Either monads, and the values
 * are extracted to allow composition of types, just like for-comprehension does.
 * It will Always return Right type.
 * In case any of the Either is Left the whole composition stop and it will return Left with that value
 */
private fun eitherFx() {
    val eitherRight: Either<Nothing, String> = Either.fx {
        val right1 = !Right("hello ")
        val right2 = !Right("either ")
        val right3 = !Right("world ")
        val right4 = !Right("sequentially")
        right1 + right2 + right3 + right4
    }
    println(eitherRight)

    val leftRight: Either<Int, String> = Either.fx {
        val right1 = !eitherString("hello ")
        val right2 = !eitherString("option ")
        val right3 = !eitherString("world ")
        val right4 = !eitherString("sequentially")
        right1 + right2 + right3 + right4
    }
    println(leftRight)
}

fun maybeString(value: String): Option<String> {
    return if (Random.nextBoolean()) {
        Some(value)
    } else {
        None
    }
}

fun eitherString(value: String): Either<Int, String> {
    return if (Random.nextBoolean()) {
        Right(value)
    } else {
        Left(1981)
    }
}

/**
 * Extract Transform Wrap
 * ----------------------
 */
/**
 * This patter is about to create an ADT(Algebra data type) which expect to receive in the constructor a type
 * <T> and implement three functions.
 * [get] to unwrap the value and return it.
 * [map] to pass a function that invoke [get] to unwrap value and apply a function over that value to transform into <S>.
 * [flatMap] to pass a function that invoke [get] to unwrap value and apply a function over that value to transform into ADT<S>.
 */
class CustomMonad<T> private constructor(private val value: T) {

    companion object {
        fun <T> pure(value: T): CustomMonad<T> {
            return CustomMonad(value)
        }
    }

    fun get(): T {
        return value
    }

    fun <S> map(fn: (T) -> S): CustomMonad<S> {
        return CustomMonad(fn(get()))
    }

    fun <S> flatMap(fn: (T) -> CustomMonad<S>): CustomMonad<S> {
        return fn(get())
    }
}

/**
 * This generic monad can use potentially with all types, and provide pattern to extract, transform and wrap the value.
 * Also it provide composition thanks to the flatMap
 */
fun customMonad() {
    val value: String =
        CustomMonad.pure("politrons")
            .flatMap { value -> CustomMonad.pure("$value!!!") }
            .map { value -> value.toUpperCase() }
            .get()
    println(value)

    val number: Int =
        CustomMonad.pure(1981)
            .flatMap { value -> CustomMonad.pure(value * 10) }
            .map { value -> value + 1000 }
            .get()
    println(number)
}

/**
 * Coroutine Monad
 * ----------------
 */
/**
 * Monad that wrap the type T and run each function in a [coroutine]
 */
class CoroutineMonad<T> private constructor(private val value: T) {

    companion object {
        fun <T> pure(value: T): CoroutineMonad<T> {
            return CoroutineMonad(value)
        }
    }

    suspend fun get(): T {
        return withContext(Dispatchers.Default) {
            value
        }
    }

    suspend fun <S> map(func: (T) -> S): CoroutineMonad<S> {
        return CoroutineMonad(
            withContext(Dispatchers.Default) {
                func(get())
            }
        )
    }

    suspend fun <S> flatMap(func: (T) -> CoroutineMonad<S>): CoroutineMonad<S> {
        return CoroutineMonad(
            withContext(Dispatchers.Default) {
                func(get()).get()
            })
    }
}

suspend fun customCoroutine() {
    val result = CoroutineMonad.pure("politrons working async")
        .flatMap { value ->
            println("Thread info ${Thread.currentThread().name}")
            CoroutineMonad.pure("$value!!!")
        }
        .map { value ->
            println("Thread info ${Thread.currentThread().name}")
            value.toUpperCase()
        }.get()
    println(result)
}


