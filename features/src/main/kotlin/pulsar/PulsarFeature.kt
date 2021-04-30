package main.kotlin.pulsar

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.pulsar.client.api.Consumer
import org.apache.pulsar.client.api.Producer
import org.apache.pulsar.client.api.PulsarClient
import org.apache.pulsar.client.api.Schema
import java.util.*

/**
 * If I have seen further it is by standing on the shoulders of giants.
 * Code based on previous example repo. https://github.com/ypt/experiment-kotlin-pulsar
 *
 * In order to execute this example of Producer and Consumer with [Pulsar] you will need [Docker] and
 * run the Pulsar image
 *
 *  docker run -it \
 *           -p 6650:6650 \
 *           -p 8080:8080 \
 *           -v $PWD/data:/pulsar/data \
 *           apachepulsar/pulsar:2.6.0 \
 *           bin/pulsar standalone
 *
 */
suspend fun main() {
    val client = createPulsarClient()
    subscribeConsumer(client)
    delay(1000)
    sendEvents(createProducer(client))
}

/**
 * Just like in Kafka, the consumer and producer it will point to a topic where to consume and produce events.
 */
const val TOPIC = "my-topic"

/**
 * Using Factory interface [PulsarClient] we use the builder to create our pulsar client, poitring to the Pulsar broker.
 * and that can be used to create the [Producer] and [Consumer]
 */
private fun createPulsarClient(): PulsarClient {
    return PulsarClient.builder()
            .serviceUrl("pulsar://localhost:6650")
            .build()
}

/**
 * Using the previous [PulsarClient] created, we can invoke [newProducer] builder to create [Producer<T>]
 * where we have top specify thr topic where to publish the events.
 */
private fun createProducer(client: PulsarClient): Producer<String> {
    return client.newProducer(Schema.STRING)
            .topic(TOPIC)
            .create()
}


/**
 * Receiving the pulsar [Producer] crated previously, inside a loop of 10 iterations, we use [sendAsync]
 * to send the event, and receive a [CompletableFuture] where we implement a callback [whenComplete] to check
 * success or error channel, to guarantee when the event has been sent to Pulsar.
 */
private fun sendEvents(producer: Producer<String>) {
    for (msgId in 1 until 10) {
        producer.sendAsync("Message-$msgId")
                .whenComplete { pulsarMsgId, error ->
                    if (error != null) {
                        println("Error sending message $msgId. Caused by $error")
                    } else {
                        println("Message $msgId sent $pulsarMsgId")
                    }
                }
    }
}

/**
 * Using the [PulsarClient] using [newConsumer] builder we create a Pulsar consumer, specifying the [topic] where to
 * consume, and the [subscriptionName](just like Kafka consumer group) to obtain the events for that group
 * It works like [consumerGroup] of Kafka, where multiple consumers can subscribe to the topic and receive the
 * copy of the message.
 */
private fun subscribeConsumer(client: PulsarClient) {
    val subscriptionName = "my-pulsar-subscription-${UUID.randomUUID()}"
    println("Loading consumer on subscription $subscriptionName and topic $TOPIC")
    val consumer = client.newConsumer()
            .topic(TOPIC)
            .subscriptionName(subscriptionName)
            .subscribe()

    consumeEvents(consumer, subscriptionName)
}

/**
 * To keep the consumer consuming constantly new events, and not blocking the main thread. We run the process
 * in another Thread using [GlobalScope.launch] inside we have a infinite loop where in each iteration,
 * we use [consumer.receive()] which it block until a new message arrive.
 * Once we process the event and we use, we do the ACK using [consumer.acknowledge(msg)] to let
 * know Pulsar that we want to receive the next message from the topic.
 */
private fun consumeEvents(consumer: Consumer<ByteArray>, subscriptionName: String) {
    GlobalScope.launch {
        while (true) {
            runCatching {
                val msg = consumer.receive()
                println("Subscription: $subscriptionName Received: ${String(msg.data)}")
                consumer.acknowledge(msg)
            }.onFailure { e ->
                println("Error consuming events. Caused by $e")
            }
        }
    }
}