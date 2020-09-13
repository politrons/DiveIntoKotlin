package main.kotlin.monads


interface FunctionalAPI<F, T> {
    fun <B> map(func: (T) -> B): F
    fun <B> flatMap(func: (T) -> Option<B>): F
    fun filter(func: (T) -> Boolean): F
}
