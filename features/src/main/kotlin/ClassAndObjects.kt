package main.kotlin

fun main() {
    println(MyFirstKotlinClass())
    println(ClassWithConstructor("Hello Kotlin Class", "World"))
    println(ClassWithMultipleConstructor("Hello Kotlin Class"))
    println(ClassWithMultipleConstructor(1981))
    println(ClassNotInstantiateByConstructor.create())
    println(SonClass().getNumber())
    println(MyFirstInterfaceImpl().getName())
    val (name, age) = returnMultiValueFromDataClass()
    println(name)
    println(age)
    copyClassValue()
    val man: Human = Man("politrons")
    println(man)
    println(Colors.RED)
    Colors.values().forEach { color -> println(color) }
    SingletonObj.printHelloKotlin()
    ClassWithCompanionObject.create()
    val myString: MyStringType = "Hello TypeAlias in Kotlin"
    println(myString)
    DelegateClass(MainClassImpl()).myDelegateMethod()
}

/**
 * Kotlin has a cool default feature, it does not allow null values for types
 */
//val nullNotAllowed:String = null // it wont compile.

/**
 * To allow null types you need to specify [?] at the end of the type
 */
val nullAllowed: String? = null

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
data class Man(val name: String, val age: Int = 40) : Human()
data class Woman(val name: String, val age: Int = 40) : Human()

/**
 * When you return a class instance, from the invoker side, you cam unwrap the values from
 * the instance.
 *     val (name, age) = returnMultiValueFromDataClass()
 */
fun returnMultiValueFromDataClass(): Man {
    return Man("politrons", 40)
}

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
 * [Companion Objects] We can create companion object in a more readable way than in scala just adding inside the class [companion] follwed
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

/**
 * Another feature exactly like in Scala, defining typeAlias for types, using [typealias] amd then pointing to a type
 * ,you can use that type but with another name.
 */
typealias MyStringType = String

/**
 * [Delegate pattern] Apply delegate pattern in Kotlin is quite simple and handy. You just need to pass in the
 * constructor the class where you want to delegate, and using the composition of [Class by instanceName]
 * automatically you class can access to all members of the passed class without have to use the instance.
 */
interface MainClass {
    fun sayHello()
}

class MainClassImpl : MainClass {
    override fun sayHello() = println("Hello delegate class")
}

class DelegateClass(m: MainClass) : MainClass by m {
    fun myDelegateMethod() = sayHello()
}