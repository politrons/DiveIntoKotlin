package main.kotlin

/**
 * Infix is a really cool feature to build DSL in Kotlin, it allow you to create a function to be invoked between
 * two values. The infix function require that you can only pass one argument, which is will be the one of the right
 * of the operator in the middle.
 */
fun main() {
    println("Hello world" isEqualTo "Hello world")
    println("foo" isDifferentFrom "bla")
    println(1 isEqualTo 1)
    println(10 mas 10)
    println(100 whoIsBigger 90)
    println("politrons" isLastElement listOf("foo", "bla", "politrons"))
    println(1981 isLastElement listOf(1, 1981, 2))
}

/**
 * Get one element into the left and check if is equal with the one of the right
 */
infix fun <T> T.isEqualTo(other: T): Boolean {
    return other == this
}

/**
 * Get one element into the left and check if is not equal with the one of the right
 */
infix fun <T> T.isDifferentFrom(other: T): Boolean {
    return other != this
}

/**
 * Get one element into the left and sum the one of the right
 */
infix fun Int.mas(other: Int): Int {
    return other + this
}

/**
 * Get one element into the left and check if is bigger than the one of the right
 */
infix fun Int.whoIsBigger(other: Int): Int {
    return if (this > other) this else other
}

/**
 * Get one element into the left and check if is the last element on the list of the right.
 */
infix fun <T> T.isLastElement(list: List<T>): Boolean {
    return list.last() == this
}
