package main.kotlin

fun main() {
    accessList()
    creationList()
    monadList()
}

/**
 * In order to create list we can use th functions that kotlin provide automatically, without any imports
 * just using [listOf] adding initial values, we will create an immutable list.
 * In case we want to filter the nulls values from initial list we just need to use operator [listOfNotNull]
 * Finally in the remote case  you just want to create a mutable, you can do it using [mutableListOf]
 */
fun creationList() {
    val strings = listOf("hello", "Kotlin", "world")
    println(strings.map { word -> word.toUpperCase() })

    val listOfNotNull = listOfNotNull("hello", null, "kotlin", null, "world")
    println(listOfNotNull)

    val mutableListOf = mutableListOf<Double>()
    mutableListOf.add(1981.0)
    println(mutableListOf)
}

/**
 * Access elements from list is not any different from any other functional language, you can obtain index element using []
 * get the [first] [last] element using those operators, reverse the List using [asReversed]
 */
fun accessList() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(numbers[0])
    println(numbers.first())
    println(numbers.last())
    println(numbers.asReversed())
}

/**
 * List collections implementations in Kotlin are Monads just like in Scala or latest Java version.
 * So all monad operators are in place, like transformation [map] composition [flatMap] [filter]
 *
 */
fun monadList() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    val filterNumbers = numbers.filter { number -> number > 5 }
    println("Filter $filterNumbers")

    val maybeFive = numbers.find { number -> number == 5 }
    println("Maybe Monad: $maybeFive")

    val strings = listOf("hello", "Kotlin", "world")

    val numbersWithoutNulls = listOfNotNull(1, 2, null, 3, 4, null, 5)

    val pairStringAndNumber = strings
        .flatMap { word ->
            numbersWithoutNulls
                .map { number -> Pair(word.toUpperCase(), number * 10) }
        }
    println("Pair $pairStringAndNumber")
}