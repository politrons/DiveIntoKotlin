package main.kotlin.patterns

import main.kotlin.lowerCaseFunc
import main.kotlin.upperCaseFunc

/**
 * In [Command] pattern we create all functions with same signature [A -> B]
 * Then we create a class that receive in constructor that Command signature [A -> B]
 * and implement an [apply] function that receive this type [A] and return [B]
 */
fun main() {
    ManageCommand(lowerCaseStringFunc).apply("HELLO WORLD")
    ManageCommand(upperCaseStringFun).apply("hello world")
    println(ManageCommand(multiplyBy100Fun).apply(1981))
    println(ManageCommand(higherOrderFunc).apply("Hello")(100))
}

/**
 * Command functions that contains all logic to be invoked in ManagedCommand class
 */
val lowerCaseStringFunc: (String) -> Unit = { a -> println(lowerCaseFunc(a)) }
val upperCaseStringFun: (String) -> Unit = { a -> println(upperCaseFunc(a)) }
val multiplyBy100Fun: (Int) -> Int = { a -> a * 100 }
val higherOrderFunc: (String) -> (Int) -> String = { a -> println("$a creating function ");{ b -> "$b is a number" } }

/**
 * Class that it created with a [Command] and invoke this command once the [apply] function is invoked.
 * Having this class we can have this generic implementation that can have different behavior depending of the
 * [Command] we pass in the constructor
 */
class ManageCommand<A, B>(val command: (A) -> B) {
    fun apply(value: A): B {
        println("Doing some logic.....")
        return command(value)
    }
}