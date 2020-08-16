package main.kotlin

fun main() {
    println(MyFirstKotlinClass())
    println(ClassWithConstructor("Hello Kotlin Class", "World"))
    println(ClassWithMultipleConstructor("Hello Kotlin Class"))
    println(ClassWithMultipleConstructor(1981))
    println(ClassNotInstantiateByConstructor.create())
    println(SonClass().getNumber())
    println(MyFirstInterfaceImpl().getName())
    copyClassValue()
    val man: Human = Man("politrons")
    println(man)
    println(Colors.RED)
    Colors.values().forEach { color -> println(color) }
    SingletonObj.printHelloKotlin()
    ClassWithCompanionObject.create()
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
class ClassNotInstantiateByConstructor private constructor() {
    companion object {
        fun create(): ClassNotInstantiateByConstructor {
            return ClassNotInstantiateByConstructor()
        }
    }
}

/**
 * In Kotlin by default all classes are final, the only way to make it extendable is setting the
 * class with keyword [open]
 * To allow a variables/method to be overridable we need to mark it also as [open]
 */
open class ClassToBeExtended {

    open val value = 0

    fun getHello() = "Hello Son"

    open fun getNumber(): Int = 10
}

/**
 * Extend a class in Kotlin you just need to use [:] and set the class to extend After.
 * To override a variable/method you just need to use [override]
 */
class SonClass : ClassToBeExtended() {
    init {
        println(getHello())
    }

    override val value: Int = 2000
    override fun getNumber(): Int = 1981
}

/**
 * Nothing special with interfaces, if you know Java it's pretty much the same.
 * Allows add fun with implementations as defaults in Java does.
 */
interface MyFirstInterface {
    fun getName(): String

    fun sayHello(): String = "hello"
}

class MyFirstInterfaceImpl : MyFirstInterface {
    override fun getName(): String {
        return sayHello() + " Politrons"
    }
}

/**
 * [Data class] are pretty much like case class in Scala, provide a final class with all equal/hashCode/toString()
 * methods, and also copy() to create a copy of a instance changing the specify attribute.
 * With Destructuring classes, we can extract variables from a data class just declaring the same number
 * of variables as constructor has, before the instance of the class.
 */
data class MyDataClass(val value1: String, val value2: String)

fun copyClassValue() {
    val myDataClass = MyDataClass("hello", "Kotlin")
    val copyClass: MyDataClass = myDataClass.copy(value1 = "New Hello")
    println(copyClass)
    val (value1, value2) = copyClass
    println("$value1-$value2")
}

/**
 * Just like in Scala with sealed trait
 */
sealed class Human
data class Man(val name: String) : Human()
data class Woman(val name: String) : Human()

/**
 * Just like Java enums
 */
enum class Colors {
    RED, WHITE, BLACK
}

/**
 * Just after Scala Kotlin apply the Singleton patter exactly the same way that Scala does.
 * Define [object] and directly all the access is static and does not require instantiate.
 */
object SingletonObj {
    fun printHelloKotlin() = println("Hello Kotlin from static singleton obj")
}

/**
 * We can create companion object in a more readable way than in scala just adding inside the class [companion] follwed
 * by object, with or without name (without you will use the same class name) and then automatically it can be used
 * statically inside and out of the class.
 */
class ClassWithCompanionObject {

    companion object {
        const val ConstValue = 1981
        fun create() = ClassWithCompanionObject()
    }

    init {
        println(ConstValue)
    }
}