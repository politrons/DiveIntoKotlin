package main.kotlin.patterns

fun main() {
    val manInfo = HumanInfo(Man())
    val womanInfo = HumanInfo(Woman())

    println(manInfo.sayName())
    println(manInfo.saySex())

    println(womanInfo.sayName())
    println(womanInfo.saySex())
}

/**
 * Decorator patter is an implementation of a class that receive in constructor another class that it
 * will use internally when the functions are invoked.
 * Having this we can have an implementation of [HumanInfo] without have to know about the implementation
 * of [Human] so we can have IoC
 */
class HumanInfo(private val human: Human) {

    fun sayName(): String {
        return human.sayName()
    }

    fun saySex(): Sex {
        return human.saySex()
    }
}

/**
 * Interface to be implemented by final classes that they will be hidden by the one that it will use
 * the class [HumanInfo] once is instantiated
 */
interface Human {

    fun sayName(): String

    fun saySex(): Sex
}

/**
 * Man implementation of [Human] hidden for another layer
 */
class Man : Human {
    override fun sayName(): String {
        return "Politrons"
    }

    override fun saySex(): Sex {
        return Male
    }
}

/**
 * Woman implementation of [Human] hidden for another layer
 */
class Woman : Human {
    override fun sayName(): String {
        return "Politrona"
    }

    override fun saySex(): Sex {
        return Female
    }
}

sealed class Sex

object Male : Sex()

object Female : Sex()



