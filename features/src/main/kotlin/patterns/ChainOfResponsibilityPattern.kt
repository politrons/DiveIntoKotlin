package main.kotlin.patterns

import main.kotlin.upperCaseFunc


fun main() {
    println(intFunction.orElse(stringFunction).apply(1981))
    println(intFunction.orElse(stringFunction).apply("hello world"))
}

/**
 * Chain of responsibility pattern allow us pass two functions and in execution decide depending of the value we receive,
 * to pass the execution to one function or the next one in the chain of responsibility.
 *
 * We use [Extension Function] where we expect the Int type as extension type, and as argument we receive a String
 * function type.
 *
 * It return a [OrElse] class that implement the [apply] function to execute one or another function depending of
 * generic value [T]
 */
fun ConsumerFunc<Int>.orElse(default: ConsumerFunc<String>): OrElse {
    return OrElse(this, default)
}

/**
 * This class contains the two functions, and in runtime once receive the value to passed to the function [apply],
 * it decide to pass the argument to the first function, or to the next one.
 */
class OrElse(val intFunc: ConsumerFunc<Int>, val stringFunc: ConsumerFunc<String>) {
    fun <T> apply(value: T): T {
        return when (value) {
            is Int -> intFunc(value) as T
            is String -> stringFunc(value) as T
            else -> throw IllegalArgumentException("Unknown element")
        }
    }
}

typealias ConsumerFunc<T> = (T) -> T

val intFunction: (Int) -> Int = { a -> a * 1000 }

val stringFunction: (String) -> String = { s -> upperCaseFunc(s) }

