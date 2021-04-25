package main.kotlin.patterns

import kotlinx.coroutines.runBlocking

/**
 * Runner of the pattern, where we create an instance [PublisherClass].
 * Once we create several subscribers for the example, the publisher invoke all of them.
 */
fun main() {
    val publisher = PublisherClass()
    publisher.addSubscriber(1, StringSubscriber())
    publisher.addSubscriber(2, IntSubscriber())
    publisher.addSubscriber(3, UserSubscriber())


    publisher.invokeSubscriber(1, "Something happens wake up")
    publisher.invokeSubscriber(2, 1981)
    publisher.invokeSubscriber(3, User("politrons", 40))

    //Wait for Async process to finish
    Thread.sleep(1000)
}

/**
 * The Publisher class contains the [addSubscriber] where we add the subscriber with the id.
 * Since [invokeSubscriber] is an async operation, we have top wrap into [runBlocking]
 * We get the subscriber using the id, and using [?] elvis operator, we're able to control the null elements of map.
 * Once we have the subscriber we invoke the interface function and we pass the generic data.
 */
class PublisherClass {
    private val subscribers: MutableMap<Int, Subscriber> = mutableMapOf()

    fun addSubscriber(id: Int, subscriber: Subscriber) {
        subscribers[id] = subscriber
    }

    fun <T> invokeSubscriber(id: Int, data: T) {
        runBlocking {
            subscribers[id]?.invoke(data)
        }
    }

}

/**
 * In this class we define the interface [Subscriber] that have thr generic [invoke] function.
 * We create [StringSubscriber] and also [IntSubscriber] that implement [invoke]
 */
interface Subscriber {
    fun <T> invoke(data: T)
}

class StringSubscriber : Subscriber {
    override fun <String> invoke(data: String) {
        println("Data:$data")
        println("Doing some logic in String subscriber")
    }
}

class IntSubscriber : Subscriber {
    override fun <Int> invoke(data: Int) {
        println("Data:$data")
        println("Doing some logic in Int subscriber")
    }
}

class UserSubscriber : Subscriber {
    override fun <User> invoke(data: User) {
        println("Data:$data")
        println("Doing some logic in User subscriber")
    }
}

data class User(val name: String, val age: Int)



