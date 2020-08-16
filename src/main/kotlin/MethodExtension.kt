package main.kotlin


fun main() {
    "Method Extension is soo cool".PrintExtensionIsCool("!!!")
}


infix fun String.PrintExtensionIsCool(that:String) = println(this)

infix fun String.XXX(that: String): Pair<String, String> = Pair(this, that)

