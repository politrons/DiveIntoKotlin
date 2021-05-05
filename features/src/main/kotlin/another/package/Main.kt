package main.kotlin.another.`package`

import kotlinx.coroutines.runBlocking
import main.kotlin.reactive.bulkHeadPattern
import main.kotlin.creationList
import main.kotlin.printAllTypes
import main.kotlin.simpleChannel

/**
 * Kotlin just allow run files without being in a class, just like Golang or Haskell,
 * You just need to create a file ended with <kt> extension.
 * To create a runner in Kotlin you just need to create main() method.
 * You can also invoke functions from another files just importing the function
 */
fun main() {
    println("Hello Kotlin world")
    creationList()
    runBlocking { simpleChannel() }
    "Hello!!".printAllTypes()
    runBlocking { bulkHeadPattern() }
}

