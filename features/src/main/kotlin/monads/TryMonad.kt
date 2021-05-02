package main.kotlin.monads

import main.kotlin.upperCaseFunc


/**
 * Contract of Monad Try<T> where we define the interface of the most commons functions of the monad
 * to allow transformation, filter and composition.
 */
sealed class Try<T> {
    /**
     * Factory method to create the Try monad, passing a Supplier function,
     * which in case is ok we return a [Success] otherwise we return a [Failure]
     */
    companion object {
        fun <T> of(action: () -> T): Try<T> {
            val runCatching: Result<T> = runCatching(action)
            return when {
                runCatching.isFailure -> Failure(runCatching.exceptionOrNull()!!)
                else -> Success(runCatching.getOrThrow())
            }
        }
    }

    /**
     * Contracts
     * ----------
     */
    abstract fun isSuccess(): Boolean

    abstract fun isFailure(): Boolean

    abstract fun get(): T

    abstract fun <B> map(func: (T) -> B): Try<B>

    abstract fun <B> flatMap(func: (T) -> Try<B>): Try<B>

    abstract fun filter(func: (T) -> Boolean): Try<T>

    abstract fun <B> foldLeft(v: B, func: (B, T) -> B): Try<B>

    abstract fun <B> foldRight(v: B, func: (T, B) -> B): Try<B>

    abstract fun <B> foreach(func: ((T) -> B))
}

/**
 * Implementation of Try interface, in case is [Success]
 */
class Success<T>(var value: T) : Try<T>() {

    override fun get(): T {
        return this.value
    }

    /**
     * Function to transform a value of type T into B
     * In case is ok we return a [Success] otherwise we return a [Failure]
     */
    override fun <B> map(func: (T) -> B): Try<B> {
        val runCatching = kotlin.runCatching { func(value) }
        return when {
            runCatching.isFailure -> Failure(runCatching.exceptionOrNull()!!)
            else -> Success(runCatching.getOrThrow())
        }
    }

    /**
     * Function to make a composition with another Monads Try.
     * Receive a value T and return a Try<B>
     * In case is ok we return a [Success] otherwise we return a [Failure]
     */
    override fun <B> flatMap(func: (T) -> Try<B>): Try<B> {
        val runCatching = kotlin.runCatching { func(value) }
        return when {
            runCatching.isFailure -> Failure(runCatching.exceptionOrNull()!!)
            else -> runCatching.getOrThrow()
        }
    }

    /**
     * Predicate Function to filter the value passed to the function, in case of false
     * we return null as value otherwise we propagate the value.
     * In case is ok we return a [Success] otherwise we return a [Failure]
     */
    override fun filter(func: (T) -> Boolean): Try<T> {
        val runCatching = kotlin.runCatching { func(value) }
        return when {
            runCatching.isFailure -> Failure(runCatching.exceptionOrNull()!!)
            else -> {
                return if (runCatching.getOrThrow()) {
                    Success(value)
                } else {
                    Failure(NoSuchElementException("Predicate does not hold for " + value))
                }
            }
        }
    }

    /**
     * Function that receive a value of type B to passed to other BiFunction as the left side
     * of the function's arguments, and return a Try<B>
     */
    override fun <B> foldLeft(v: B, func: (B, T) -> B): Try<B> {
        val runCatching = kotlin.runCatching { func(v, value) }
        return when {
            runCatching.isFailure -> Failure(runCatching.exceptionOrNull()!!)
            else -> Success(runCatching.getOrThrow())
        }
    }

    /**
     * Function that receive a value of type B to passed to other BiFunction as the right side
     * of the function's arguments, and return a Try<B>
     */
    override fun <B> foldRight(v: B, func: (T, B) -> B): Try<B> {
        val runCatching = kotlin.runCatching { func(value, v) }
        return when {
            runCatching.isFailure -> Failure(runCatching.exceptionOrNull()!!)
            else -> Success(runCatching.getOrThrow())
        }
    }

    /**
     * Just apply the function using value of the instance.
     * Since [foreach] return Unit the value passed in the constructor is [var]
     * to allow mutability
     */
    override fun <B> foreach(func: (T) -> B) {
        func(value)
    }

    override fun isSuccess(): Boolean = true

    override fun isFailure(): Boolean = false

}

/**
 * Implementation of Try interface, in case is [Failure]
 */
class Failure<T>(private val throwable: Throwable) : Try<T>() {

    /**
     * Just return the throwable passed in the constructor
     */
    override fun get(): T = throw throwable

    override fun <B> map(func: (T) -> B): Try<B> = this as Try<B>

    override fun <B> flatMap(func: (T) -> Try<B>): Try<B> = this as Try<B>

    override fun filter(func: (T) -> Boolean): Try<T> = this

    override fun <B> foldLeft(v: B, func: (B, T) -> B): Try<B> = this as Try<B>

    override fun <B> foldRight(v: B, func: (T, B) -> B): Try<B> = this as Try<B>

    override fun <B> foreach(func: (T) -> B) = Unit

    override fun isSuccess(): Boolean = false

    override fun isFailure(): Boolean = true
}

fun main() {

    val succeedResult = Try.of { invokeFunc() }
        .map { a -> upperCaseFunc(a) }
        .flatMap { b -> Try.of { "$b!!!" } }
        .get()
    println("Succeed result:$succeedResult")

    val filterSucceedResult = Try.of { invokeFunc() }
        .map { a -> upperCaseFunc(a) }
        .filter { a -> a.length > 5 }
        .get()

    println("Filter succeed result:$filterSucceedResult")

    val filterSkipResult = runCatching {
        Try.of { -> invokeFunc() }
            .map { a -> upperCaseFunc(a) }
            .filter { a -> a.length > 100 }
            .get()
    }.exceptionOrNull()

    println("Filter skip result:$filterSkipResult")

    val foldLeftResult = Try.of { invokeFunc() }
        .foldLeft("$$$", { a, b -> upperCaseFunc(a + b) })
        .get()
    println("FoldLeft succeed result:$foldLeftResult")

    val foldRightResult = Try.of { invokeFunc() }
        .foldRight("$$$", { a, b -> upperCaseFunc(a + b) })
        .get()

    println("FoldRight succeed result:$foldRightResult")

    val failureResult = runCatching {
        Try.of { invokeFunc() }
            .map { a -> upperCaseFunc(a[100].toString()) }
            .get()
    }.exceptionOrNull()
    println("Error result:$failureResult")
}

fun invokeFunc(): String {
    return "hello monad try"
}
