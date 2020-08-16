package main.kotlin

fun main() {
    creationList()
    accessList()
    monadList()
    creationMap()
    accessMap()
}
/**
 * List features
 * --------------
 */
/**
 * In order to create list we can use th functions that kotlin provide automatically, without any imports
 * just using [listOf] adding initial values, we will create an immutable list.
 * In case we want to filter the nulls values from initial list we just need to use operator [listOfNotNull]
 * Finally in the remote case  you just want to create a mutable, you can do it using [mutableListOf]
 * To concat two list you just need to use [+] or [plus] operator
 */
fun creationList() {
    val strings = listOf("hello", "Kotlin", "world")
    println(strings.map { word -> word.toUpperCase() })

    val listOfNotNull = listOfNotNull("hello", null, "kotlin", null, "world")
    println(listOfNotNull)

    val mutableListOf = mutableListOf<Double>()
    mutableListOf.add(1981.0)
    println(mutableListOf)

    val concatList = strings.plus(listOf("!!!!!"))
    val concatListSugar = strings + listOf("Sugar!")
    println("Concat list: $concatList")
    println("Concat list sugar: $concatListSugar")
}

/**
 * Access elements from list is not any different from any other functional language, you can obtain index element using []
 * get the [first] [last] element using those operators, reverse the List using [asReversed]
 * [forEach] operator pass a Consumer function and apply that function for each element
 */
fun accessList() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    println(numbers[0])
    println(numbers.first())
    println(numbers.last())
    println(numbers.asReversed())
    numbers.forEach { n -> println(n) }
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

    /**
     * In this composition we can see how we can create tuple [Pair] in Kotlin.
     * We can create [Pair] and wrap two values, or just use infix [to] in between both values.
     */
    val pairStringAndNumber = strings
        .flatMap { word ->
            numbersWithoutNulls
                .map { number -> word.toUpperCase() to number * 10 }
        }
    println("Pair $pairStringAndNumber")
}

/**
 * Map features
 * --------------
 */
/**
 * Map collection it's quite similar to List where we have some functions to create immutable maps
 *  using [mapOf] where we can pass n [Pair] using Pair or sugar [to]
 *  To obtain a value we just need to use [key] to search for that key and obtain the value
 * We can also concat maps just using like in list [plus] or sugar [+] operator
 */
fun creationMap() {
    val myMap = mapOf("foo" to "bla", Pair("key", "Value"))
    println(myMap)
    println(linkedMapOf("key" to "value"))
    println(sortedMapOf(Pair("key", "value")))

    val concatMap = mapOf(1 to "hello") + mapOf(2 to "kotlin")
    println("Concat map: $concatMap")
}

/**
 * Access to map is similar like in list, we just use [key] this time instead of index, we specify
 * the key of the entry we want to obtain.
 * We can also obtain the [set] of keys and [collection] for values.
 * [entries] return a list of Entry which contains the key/values
 */
fun accessMap() {
    val myMap = mapOf("1" to "bla", Pair("2", "Value"))
    println(myMap["key"])
    println(myMap.keys)
    println(myMap.values)
    val entryList = myMap.entries.map { entry -> entry.key to entry.value }.toList()
    println(entryList)
}


