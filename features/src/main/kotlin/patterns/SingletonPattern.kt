package main.kotlin.patterns

fun main() {
    println(SingletonObject.hello())
    println(SingletonClass.create("Single instance")?.hello())
    println(SingletonClass.create("Single instance")?.hello())
}

/**
 * Easiest and more natural way to have singleton in Kotlin, just like in Scala, is using [object]
 * which guarantee that you cannot have more than one instance.
 */
object SingletonObject {
    fun hello(): String = "How you doing?"
}

/**
 * A most verbose and not recommended way to do it, is how we would implement the pattern in Java.
 * We create a static access in the class using companion object where we insantiate the class,
 * and we store in a mutable variable, so it means it have to be a possible nullable value,
 * which is the reason we mark the return class type with [?]
 */
class SingletonClass private constructor(val value: String) {

    companion object {
        private var instance: SingletonClass? = null

        fun create(value: String): SingletonClass? = run {
            if (instance == null) {
                println("Initializing class.....")
                instance = SingletonClass(value)
            }
            instance
        }
    }

    fun hello(): String = "How you doing boomer?"
}

