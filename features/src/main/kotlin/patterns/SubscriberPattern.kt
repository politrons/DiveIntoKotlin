package main.kotlin.patterns

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Runner of the pattern, where we create an instance [SubscriberPattern] specifying the generic type
 * of the channel.
 * Once we create several subscribers for the example, the publisher invoke one of them.
 * Since is an async operation, have top wrap into [runBlocking]
 * We get the subscriber using a random number where the range is higher on purpose to show how we're
 * able to control the null elements of map using [?] elvis operator
 */
fun main() {
    val subscriberPattern = SubscriberPattern<String>()
    subscriberPattern.addSubscriber(1)
    subscriberPattern.addSubscriber(2)
    subscriberPattern.addSubscriber(3)
    subscriberPattern.addSubscriber(4)

    runBlocking {
        subscriberPattern.subscribers[(0..4).random()]?.send("Something happens wake up")
    }
    //Wait for Async process to finish
    Thread.sleep(1000)
}

/**
 * In this class we define the generic type to specify the channel type.
 * We also define a mutable map, where we add all channel subscribers, which
 * the publisher will invoke passing the type [T]
 * In order to add the logic of what to do once the subscriber is invoked asynchronous,
 * We wrap into [GlobalScope.launch] and then we block the channel waiting for a data T to arrive
 * using [for] loop
 */
class SubscriberPattern<T> {

    val subscribers: MutableMap<Int, Channel<T>> = mutableMapOf()

    fun addSubscriber(id: Int) {
        val channel = Channel<T>(100)
        GlobalScope.launch {
            for (data in channel) {
                println(data)
                println("Doing some logic in subscriber $id")
            }
        }
        subscribers[id] = channel
    }

}

