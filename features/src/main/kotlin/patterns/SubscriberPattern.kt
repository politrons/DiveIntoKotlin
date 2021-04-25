package main.kotlin.patterns

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val subscriberPattern = SubscriberPattern<String>()
    runBlocking {
        subscriberPattern.addSubscriber(1)
        subscriberPattern.addSubscriber(2)
        subscriberPattern.addSubscriber(3)
        subscriberPattern.addSubscriber(4)

        subscriberPattern.subscribers[2]?.send("Something happens wake up")
    }
    //Wait for Async process to finish
    Thread.sleep(1000)
}

class SubscriberPattern<T> {

    val subscribers: MutableMap<Int, Channel<T>> = mutableMapOf()

    suspend fun addSubscriber(id: Int) {
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

