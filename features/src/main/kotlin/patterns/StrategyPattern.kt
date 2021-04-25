package main.kotlin.patterns

import main.kotlin.upperCaseFunc

/**
 * Similar to [Decorator] patter but not extending any function and using functions.
 * With [Strategy] patter we implement a class that receive a function in constructor, which
 * have no idea what it does. Only that receive some arguments with a type and return a type.
 *
 * Then is responsibility of the consumer of this class to implement the function and pass in the creation
 * of the class
 */
fun main() {

    ManageStrategy { a, b -> "$a  $b" }.run("hello", "world")
    ManageStrategy { a, b -> upperCaseFunc("$a  $b") }.run("hello", "world")
    ManageStrategy { a, b -> "$a?? Go to hell $b" }.run("hello", "world")

}

typealias Strategy = (String, String) -> String

/**
 * This class only know that receive a [typealias] function, and offer a [run] function to invoke the
 * function received in the constructor
 */
class ManageStrategy(private val strategy: Strategy) {

    fun run(a: String, b: String) {
        println(strategy.invoke(a, b))
    }
}

