package main.kotlin

fun main() {
    TODOFunction()
    preconditions()
    nullPointerNotAllowed()
    letFeature()
    lazyVariable()
    nothingToAvoidContinuation()
}

private fun nothingToAvoidContinuation() {
    infiniteLoop()
    println("This code it will never executed")
}

/**
 * Since I mark this function like it return [Nothing] whatever code
 * after this invocation it will marked by the compiler as unreachable code
 */
private fun infiniteLoop(): Nothing {
    while (true) {
        Thread.sleep(50000)
    }
}

/**
 * One of the best features of Kotlin language is Nullability and how by default
 * the language does not allowed.+
 * Any time you see a type with [?] means is a variable with null side-effect
 * which means the language always force you to use checking that possibility
 * using if condition or just extracting whatever internal value with [?]
 * which means that value you return is nullable as well.
 * In this example we should suffer in Java or Scala two NullPointers, but
 * thanks to this awesome feature, we just return null as final value
 */
private fun nullPointerNotAllowed() {
    val value: String? = null
    val length = value?.length
    println(length?.inc())
    println(value?.length ?: 1981)
}

/**
 * Just like in Scala ??? Here we can Specify a function [TODO] which it will
 * help us to compile our program, until we're ready to implement that part.
 * In case the program go into that part of the program, it will throw [NotImplementedError]
 */
private fun TODOFunction() {
    val value = true
    val response = if (value) {
        "good"
    } else {
        TODO("Implement me asap")
    }
}

/**
 * Just like in Java we can have preconditions to throw some exceptions that our program might be ready to catch.
 *
 * All require* preconditions throw IllegalArgumentExceptions
 * All check* preconditions throw IllegalStateExceptions
 * assert precondition throw AssertError
 */
private fun preconditions() {
    val number: Int? = 7
    requireNotNull(number) { "$number  cannot be null" }
    require(number > 5) { "$number  must be higher than 10" }

    check(number == 7) { "$number  must be  10" }

    assert(number == 7) { "$number  must be  10" }
}

/**
 * Let operator is really hamndy when we want to invoke a function once, and we want to
 * set this man in the middle of that function to obtain that value, do some stuff and end up
 * even returning a different type if we want
 */
fun letFeature() {
    val letResult = returnHello().let { result ->
        println(result)
        println("$result - $result")
        "$result!!!"
    }
    println(letResult)
}

fun returnHello(): String {
    return "hello Kotlin"
}

/**
 * Lazy variable is a little bit more verbose than in Scala, but achieve same principle.
 * Variables marked as lazy they will be evaluated once are used and not when they are declared
 */
fun lazyVariable() {
    val calNumber:Int by lazy {
        Thread.sleep(2000)
        1981
    }
    println("fast print")
    println(calNumber)
}
