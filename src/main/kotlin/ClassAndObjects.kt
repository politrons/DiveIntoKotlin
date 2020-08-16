package main.kotlin

fun main() {
    println(MyFirstKotlinClass())
    println(ClassWithConstructor("Hello Kotlin Class", "World"))
    println(ClassWithMultipleConstructor("Hello Kotlin Class"))
    println(ClassWithMultipleConstructor(1981))
    println(CannotInstantiateByConstructor.create())
    println(SonClass())
}

/**
 * Just like in all JVM lang classes are created with keyword [class]
 * If we decide we want to execute some functions in the constructor initialization, we need
 * to wrap those functions invocations in [init{}]
 */
class MyFirstKotlinClass {
    private val initVariable = "Hello constructor part"

    init {
        println(initVariable)
    }
}

/**
 * Just like Scala Kotlin add the constructor in the class definition level.
 * Also like in Scala we will use in constructor [var] to define mutable variables,
 * and [val] for immutable values.
 */
class ClassWithConstructor(var mutableVar: String, val immutableVal: String) {}

/**
 * Define second constructor in Kotlin require that you use the keyword [constructor] and
 * need to be follow by the invocation of the primary constructor as return type defined : this(T)
 */
class ClassWithMultipleConstructor(val value: String) {
    var intValue = 0

    constructor(_intValue: Int) : this("") {
        this.intValue = _intValue
    }
}

/**
 * In case we want to make private a constructor we can just set as private the keyword [constructor]
 * and use a companion object to create the static factory method
 */
class CannotInstantiateByConstructor private constructor() {
    companion object {
        fun create(): CannotInstantiateByConstructor {
            return CannotInstantiateByConstructor()
        }
    }
}

/**
 * In Kotlin by default all classes are final, the only way to make it extendable is setting the
 * class with keyword [open]
 * To allow a method to be overridable we need to mark it also as [open]
 */
open class ClassToBeExtended {
    fun getHello() = "Hello Son"

    open fun getNumber(): Int = 10
}

/**
 * Extend a class in Kotlin you just need to use [:] and set the class to extend After.
 * To override a method you just need to use [override]
 */
class SonClass : ClassToBeExtended() {
    init {
        println(getHello())
    }

    override fun getNumber():Int = 1981
}