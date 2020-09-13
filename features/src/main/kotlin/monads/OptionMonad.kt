package main.kotlin.monads

fun main() {
    optionMonad()
}

private fun optionMonad() {
    someExample()
    noneExample()
    filterExample()
    foldExample()
}

/**
 * Implementation of Option monad, to control side-effect of null/empty
 *
 * sealed class where we define the Factory functions and API that must be implemented by None in case there's no value and Some in case it has.
 *
 * We create a companion object that contains creation functions to return an Option<T> in case we use [of] operator
 * or Option<Nothing> if we use [empty]
 *
 * Also we define all common [FunctionalAPI] like map, flatMap, filter, foldLeft, foldRight, getOrElse, isDefined
 * for Option monad.
 */
sealed class Option<T> : FunctionalAPI<Option<*>, T> {
    companion object {
        fun <T> of(value: T): Option<T> {
            return if (value == null || value == "") None() else Some(value)
        }

        fun empty():Option<Nothing> {
            return None()
        }
    }

    abstract fun isDefined(): Boolean

    abstract override fun <B> map(func: (T) -> B): Option<B>

    abstract override fun <B> flatMap(func: (T) -> Option<B>): Option<B>

    abstract override fun filter(func: (T) -> Boolean): Option<T>

    abstract fun <B> fold(ifEmpty: B, func: (T) -> B): Option<B>

    abstract fun <B> foldLeft(v: B, func: (B, T) -> B): Option<B>

    abstract fun <B> foldRight(v: B, func: (T, B) -> B): Option<B>

    abstract fun getOrElse(default: T): T

    abstract fun value(): T

}

/**
 * We implement the effect that the [Option] comntain a value,so then we can allow
 * transformation with [map], composition with [flatMap] and some other operators
 * implementations defining functions.
 */
class Some<T>(private val value: T) : Option<T>() {

    /**
     * Transformation function, it receive value T and transform into value B
     */
    override fun <B> map(func: (T) -> B): Option<B> {
        return Some(func(value))
    }

    /**
     * Composition function, it receive value T and transform into Option of value B
     */
    override fun <B> flatMap(func: (T) -> Option<B>): Option<B> {
        return func(value)
    }

    /**
     * Predicate function, it receive value T and the function return a boolean
     */
    override fun filter(func: (T) -> Boolean): Option<T> {
        return if (func(value)) Some(value) else None()
    }

    /**
     * fold function where we receive the default value in case it does not exist value in option,
     * and a function to apply over the value in case it exist.
     */
    override fun <B> fold(ifEmpty: B, func: (T) -> B): Option<B> {
        return Some(func(value))
    }

    /**
     * Function to make composition between the value in the option and the one passed as first argument.
     * The second argument is a function that pass the new element first and then the one in the monad
     */
    override fun <B> foldLeft(v: B, func: (B, T) -> B): Option<B> {
        return Some(func(v, value))
    }

    /**
     * Function to make composition between the value in the option and the one passed as first argument.
     * The second argument is a function that pass the one in the monad and then the new element
     */
    override fun <B> foldRight(v: B, func: (T, B) -> B): Option<B> {
        return Some(func(value, v))
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

/**
 * In case of effect that we pass an Option with null or empty, we return in most
 * of the functions None or if it's passed a default value in a function, the default.
 */
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

    override fun <B> fold(ifEmpty: B, func: (T) -> B): Option<B> {
        return Some(ifEmpty)
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

    val fold: Option<String> = Option.of("")
            .fold("No values!!", { value -> value.toUpperCase() })
    println(fold.getOrElse(""))

    val empty: Option<Nothing> = Option.empty()
    println(empty.fold("empty value", {a -> a}).getOrElse(""))
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