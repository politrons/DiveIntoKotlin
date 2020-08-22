package main.kotlin


fun main() {
    stringWhen()
    classWhen()
}

fun stringWhen() {
    println(upperCaseFun(null))
    println(upperCaseFun("Hello when condition"))
}

private fun upperCaseFun(nullValue: String?): String {
    return when (nullValue) {
        null -> "Value does not exist"
        else -> nullValue.toUpperCase()
    }
}

fun classWhen() {
    val animalName = when (val animal: Animal = Dog("Bingo")) {
        is Dog -> "This Dog is called ${animal.name}"
        is Cat -> "This Cat is called ${animal.name}"
        else -> "I dont know this animal"
    }
    println(animalName)
}

sealed class Animal()

data class Dog(val name: String) : Animal()
data class Cat(val name: String) : Animal()
