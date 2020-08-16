package main.kotlin

data class User(val id: String, val pass: String)

/**
 * Extension function is a mechanism where we can extend third party classes and also our own implementations.
 * Is so simple like create a fun where before the name of the function we set the Type [T] we want to extend
 */
fun main() {
    "Method Extension is soo cool".printValue()
    println(1981.multiplyBy(10))
    val user = User("politrons", "myPass")
    println("Valid user:${user.login()}")
    1.printAllTypes()
    "Hello world".printAllTypes()
    user.printAllTypes()
    listOf("This", "extension", "function", "is", "awesome").printEachElement()
    listOf(1, 2, 3, 4, 5).printEachElement()
    println("Hello world".wrapIn("$$$$"))
    println(1981.wrapIn("$$$$"))
    println(listOf("Check if is null", null).find { word -> word.secureLength() > 3 })
    B(A("Hello extension function in class scope"))
}

/**
 * Ext fun of String to print the String value
 */
fun String.printValue() {
    println(this)
}

/**
 * Ext fun of Int to multiply the value by the number passed
 */
fun Int.multiplyBy(n: Int) = this * n

/**
 * Ext fun of User to login the user
 */
fun User.login(): Boolean = this.id == "politrons" && this.pass == "myPass"

/**
 * Genetic Ext fun that apply to all types and print the contain
 */
fun <T> T.printAllTypes() = println(this)

/**
 * Genetic Ext fun that apply to all List with generic types and print the contain
 */
fun <T> List<T>.printEachElement() = this.forEach { element -> println(element) }

/**
 * Genetic Ext fun that apply to all types and wrap the entry value to the extended value
 */
fun <T> T.wrapIn(c: String) = c + this + c

/**
 * Ext fun of maybe String where we can have a String or null
 */
fun String?.secureLength(): Int {
    if (this == null) return 0
    return this.length
}

/**
 * Ext fun of class A inside class B that is defined there, and we'ere able to use it,since we
 * receive the instance of A.
 * The really cool part is, that once you extend a class function you're able to access to all public elements
 * of that class.
 */
class B(a: A) {

    private fun A.getValueInUpperCase() = getValueInLowerCase().toUpperCase()

    init {
        println(a.getValueInUpperCase())
    }
}

class A(private val value: String) {
    fun getValueInLowerCase(): String = value.toLowerCase()
}



