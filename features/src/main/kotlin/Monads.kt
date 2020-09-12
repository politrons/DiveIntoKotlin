package main.kotlin


fun main() {
    optionMonad()
}

private fun optionMonad() {
    val some: Option<String> = Option.of("hello world")
            .map { value -> value.toUpperCase() }
            .flatMap { value -> Option.of("$value!!!!") }

    println("${some.isDefined()}")
    println(some.getOrElse("default"))
    println(when (some) {
        is Some -> "Extracting value ${some.value()}"
        else -> "Nothing here"
    })

    val none: Option<String> = Option.of(null)
            .flatMap { value -> Option.of("$value!!!!") }
    println("${none.isDefined()}")
    println(none.getOrElse("default"))
    println(when (none) {
        is Some -> none.value()
        else -> "Nothing here"
    })
}

sealed class Option<T> {
    companion object {
        fun <T> of(value: T): Option<T> {
            return if (value == null) None() else Some(value)
        }
    }

    abstract fun isDefined(): Boolean

    abstract fun <B> map(func: (T) -> B): Option<B>

    abstract fun <B> flatMap(func: (T) -> Option<B>): Option<B>

    abstract fun getOrElse(default: T): T

    abstract fun value():T

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
}

