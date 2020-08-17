package main.kotlin


fun main() {
    higherOrderFunc { value -> value == "hello" }
    foldFunction()
    println(upperCaseFunc)
    println(upperCaseFunc("Hello functional world in Kotlin"))
    println(curryFunc)
    println(curryFunc("hello"))
    println(curryFunc("hello")("kotlin"))
    println(curryFunc("hello")("kotlin")("functional"))
    val isMyNameFunc: (String) -> Boolean = outputFunc("username")
    println(isMyNameFunc("politrons"))
}

/**
 * In Kotlin functions are first-class citizen, which mean, that functions can receive and return functions.
 */
fun higherOrderFunc(func: (String) -> Boolean) {
    println("Is Hello? ${func("hello")}")
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
