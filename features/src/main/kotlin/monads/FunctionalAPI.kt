package main.kotlin.monads

/**
 * Genetic interface for all Functional effects monads.
 */
interface FunctionalAPI<F, T> {

    fun <B> map(func: (T) -> B): F

    fun <B> flatMap(func: (T) -> Option<B>): F

    fun filter(func: (T) -> Boolean): F

    fun <B> fold(ifEmpty: B, func: (T) -> B): F

    fun <B> foldLeft(v: B, func: (B, T) -> B): F

    fun <B> foldRight(v: B, func: (T, B) -> B): F
}
