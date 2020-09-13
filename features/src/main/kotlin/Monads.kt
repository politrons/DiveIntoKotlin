package main.kotlin


fun main() {
    optionMonad()
}

/**
 * ---------------
 * Option Monad
 * ---------------
 */

private fun optionMonad() {
    someExample()
    noneExample()
    filterExample()
    foldExample()
}

/**
 * Implementation of Option monad, to control side-effect of null/empty
 * sealed class where we define the interface that must be implemented by None in case there's no value
 *  and Some in case it has.
 * We define all common Functional API like map, flatMap, filter, foldLeft, foldRight, getOrElse, isDefined.
 */
sealed class Option<T> {
    companion object {
        fun <T> of(value: T): Option<T> {
            return if (value == null || value == "") None() else Some(value)
        }
    }

    abstract fun isDefined(): Boolean

    abstract fun <B> map(func: (T) -> B): Option<B>

    abstract fun <B> flatMap(func: (T) -> Option<B>): Option<B>

    abstract fun filter(func: (T) -> Boolean): Option<T>

    abstract fun <B> foldLeft(v: B, func: (B, T) -> B): Option<B>

    abstract fun <B> foldRight(v: B, func: (T, B) -> B): Option<B>

    abstract fun getOrElse(default: T): T

    abstract fun value(): T

}

class Some<T>(private val value: T) : Option<T>() {

    override fun <B> map(func: (T) -> B): Option<B> {
        return Some(func(value))
    }

    override fun <B> flatMap(func: (T) -> Option<B>): Option<B> {
        return func(value)
    }

    override fun getOrElse(default: T): T {
        return value ?: default
    }

    override fun isDefined(): Boolean {
        return true
    }

    override fun value(): T {
        return value
    }

    override fun filter(func: (T) -> Boolean): Option<T> {
        return if (func(value)) Some(value) else None()
    }

    override fun <B> foldLeft(v: B, func: (B, T) -> B): Option<B> {
        return Some(func(v, value))
    }

    override fun <B> foldRight(v: B, func: (T, B) -> B): Option<B> {
        return Some(func(value, v))
    }

}

class None<T> : Option<T>() {
    override fun <B> map(func: (T) -> B): Option<B> {
        return None()
    }

    override fun <B> flatMap(func: (T) -> Option<B>): Option<B> {
        return None()
    }

    override fun getOrElse(default: T): T {
        return default
    }

    override fun isDefined(): Boolean {
        return false
    }

    override fun value(): T {
        throw IllegalAccessError("No element")
    }

    override fun filter(func: (T) -> Boolean): Option<T> {
        return None()
    }

    override fun <B> foldLeft(v: B, func: (B, T) -> B): Option<B> {
        return None()
    }

    override fun <B> foldRight(v: B, func: (T, B) -> B): Option<B> {
        return None()
    }
}

/**
 * ---------------
 * Example runners
 * ---------------
 */
private fun filterExample() {
    val filter: Option<String> = Option.of("hello world")
            .filter { value -> value.length > 50 }
    println(when (filter) {
        is Some -> "Extracting value ${filter.value()}"
        else -> "Nothing here"
    })
}

private fun noneExample() {
    val none: Option<String> = Option.of(null)
            .flatMap { value -> Option.of("$value!!!!") }
    println("${none.isDefined()}")
    println(none.getOrElse("default"))
    println(when (none) {
        is Some -> none.value()
        else -> "Nothing here"
    })
}

private fun someExample() {
    val some: Option<String> = Option.of("hello world")
            .map { value -> value.toUpperCase() }
            .flatMap { value -> Option.of("$value!!!!") }
            .filter { value -> value.length > 5 }

    println("${some.isDefined()}")
    println(some.getOrElse("default"))
    println(when (some) {
        is Some -> "Extracting value ${some.value()}"
        else -> "Nothing here"
    })
}

private fun foldExample() {
    val some: Option<String> = Option.of("hello world")
            .foldRight("!!!!", { next, prev ->
                next + prev
            })
    println(when (some) {
        is Some -> "Extracting value ${some.value()}"
        else -> "Nothing here"
    })
}