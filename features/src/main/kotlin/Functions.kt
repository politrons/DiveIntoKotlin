package main.kotlin


fun main() {
    higherOrderFunc { value -> value == "hello" }
    println(higherOrderFuncReturnFun("politrons")("politrons"))
    foldFunction()
    println(upperCaseFunc)
    println(upperCaseFunc("Hello functional world in Kotlin"))
    println(curryFunc)
    println(curryFunc("hello"))
    println(curryFunc("hello")("kotlin"))
    println(curryFunc("hello")("kotlin")("functional"))
    val isMyNameFunc: (String) -> Boolean = outputFunc("username")
    println(isMyNameFunc("politrons"))
    println(inferredFunc(10, "x"))
    println(compositionResult)
}

/**
 * In Kotlin functions are first-class citizen, which mean, that functions can receive and return functions.
 */
fun higherOrderFunc(func: (String) -> Boolean) {
    println("Is Hello? ${func("hello")}")
}

/**
 * As a higher order function return functions is also common, you just need to specify the function
 * signature as return type
 */
fun higherOrderFuncReturnFun(value: String): (String) -> String {
    return when (value) {
        "POLITRONS" -> { s: String -> "hello $s how are you?" }
        else -> { s: String -> "hello $s who are you?" }
    }
}

/**
 * Fold in Kotlin works just like in all functional language, we pass a constructor element as first argument,
 * and a function with previous element accumulated and the new element of the iterator
 */
fun foldFunction() {
    val sumAllNumbers = listOf(1, 2, 3, 4, 5)
        .fold(0, { next: Int, prev: Int ->
            next + prev
        })
    println(sumAllNumbers)

    val listBy10 = listOf(1, 2, 3, 4, 5)
        .foldRight(emptyList(), { next: Int, prev: List<Int> ->
            prev + listOf(next * 10)
        })
    println(listBy10)
}

/**
 * Functions in Kotlin must specify as all functions an Input argument A and the result of the function B
 * the input arguments must be wrapped in (A) and return type can be just the type. (A) -> B
 * the implementation of the function as all functions in Kotlin must be inside brackets {}
 */
val upperCaseFunc: (String) -> String = { value -> value.toUpperCase() }

/**
 * Curry functions in Kotlin are same pattern than in the rest of FP language, you can chain
 * multiple functions together where the output of the first function is another function and so on
 * until you pass all arguments to the function, and then is applied.
 */
val curryFunc: (String) -> (String) -> (String) -> String =
    { value ->
        { value1 ->
            { value2 ->
                ("$value  $value1  $value2").toUpperCase()
            }
        }
    }

/**
 * As we describe before, it's perfectly normal that a function just return another function.
 * To allow that we just need to follow the same principle for input data, but in this case
 * for output data of the function, and wrap the whole output function in parenthesis
 */
val outputFunc: (String) -> ((String) -> Boolean) =
    { value1 ->
        if (value1 == "username") {
            { value2 ->
                value2 == "politrons"
            }
        } else {
            { value2 ->
                value2 == "foo"
            }
        }
    }

/**
 * We can also avoid define the function types if we specify directly the types as the lambda arguments.
 * Also the return type it will be inferred by the compiler
 */
val inferredFunc = { a: Int, b: String -> b.toUpperCase() + (a * 100) + b.toUpperCase() }

/**
 * Compose functions in Kotlin can be done just like in any other language, you pass the function as
 * argument of the one which expect the output of the function and so on.
 * And finally you need to just invoke the function that start everything.
 */
val lowerCaseFunc: (String) -> String = { value -> value.toLowerCase() }
val appendCharacter: (String, String) -> String = { word, character -> word + character }
val compositionResult = lowerCaseFunc(appendCharacter(lowerCaseFunc("HELLO"), " KOTLIN"))