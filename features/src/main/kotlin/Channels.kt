package main.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlin.random.Random


/**
 * Channels are exactly the same concept of GoLang channels. We create those channels for a specific type,
 * and we can use those to communicate from one coroutine to another.
 */
@ExperimentalCoroutinesApi
fun main() {
    runBlocking { simpleChannel() }
    runBlocking { producerSubscriberPattern() }
    runBlocking { consumeEach() }
    runBlocking { distributedChannelPattern() }
}

/**
 * We create a simple [Channel] specifying the type we want to contain that channel.
 * Once we have the channel we can share between Coroutines threads to communicate data.
 * To send data into the channel we just use [send] operator.
 * To subscribe to receive data we use [receive] which it will block the thread until the data is received.
 */
suspend fun simpleChannel() {
    val channel: Channel<String> = Channel()
    GlobalScope.launch {
        println("Sending data from thread ${Thread.currentThread().name}")
        channel.send("Hello world from another coroutine, traveling in a channel")
    }
    val job: Job = GlobalScope.launch {
        println("Receiving data from thread ${Thread.currentThread().name}")
        println("Data: ${channel.receive()}")
    }
    job.join()
}

/**
 * To apply this pattern we need to change our code a little bit different as we did before.
 * We can create a producer just using [GlobalScope.produce] , this time since we have a producer that it will potentially full the channel
 * provoking an outOfMemory, we set a max size of the producer of 1000 passing in the constructor.
 *
 * We send the data as before using [send]
 *
 * In the producer we can use a [for] in [channel] loop to keep it subscribed forever of the channel
 * extracting any data that is received in the channel
 */
suspend fun producerSubscriberPattern() {
    val channel: ReceiveChannel<Int> = GlobalScope.produce(capacity = 1000) {
        while (true) {
            send(Random.nextInt(100))
            delay(500)
        }
    }
    val job: Job = GlobalScope.launch {
        for (data in channel) {
            println("Data received:$data")
        }
    }
    delay(5000)
    job.cancel()
}

/**
 * We can also use [consumeEach] operator to keep the consumer waiting forever for all events written in the channel
 * As you can notice here, channels are in fact Queue, so it does not matter if you write a message into the channel
 * before we have a subscriber. Once we subscribe one, it will receive all messages
 */
@ExperimentalCoroutinesApi
suspend fun consumeEach() {
    val channel: Channel<String> = Channel(1000)
    channel.send("Message 1 send to consume each operator")
    channel.send("Message 2 send to consume each operator")
    channel.send("Message 3 send to consume each operator")
    channel.send("Message 4 send to consume each operator")
    channel.send("Message 5 send to consume each operator")
    delay(1000)
    val job: Job = GlobalScope.launch {
        channel.consumeEach { data ->
            println("Data received:$data")
        }
    }
    delay(1000)
    job.cancel()
}

/**
 * Similar pattern that you can find with Kafka consumers using same groupId. The throughput is distributed
 * in all consumers we have on the channel. This is particular useful pattern to consume message in parallel
 */
suspend fun distributedChannelPattern() {
    val channel: ReceiveChannel<Int> = GlobalScope.produce {
        while (true) {
            send(Random.nextInt(100))
            delay(500)
        }
    }
    val job1: Job = GlobalScope.launch {
        for (data in channel) {
            println("Consumer 1:$data")
        }
    }
    val job2: Job = GlobalScope.launch {
        for (data in channel) {
            println("Consumer 2:$data")
        }
    }
    val job3: Job = GlobalScope.launch {
        for (data in channel) {
            println("Consumer 3:$data")
        }
    }
    delay(5000)
    job1.cancel()
    job2.cancel()
    job3.cancel()
}