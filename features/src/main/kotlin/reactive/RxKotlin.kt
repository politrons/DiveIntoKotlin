package main.kotlin.reactive

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.toObservable
import main.kotlin.upperCaseFunc
import java.util.concurrent.CompletableFuture

/**
 * RxKotlin is basically an extension of RxJava, to allow Reactive programing in Kotlin using
 * this library.
 */
fun main() {
    singleCreation()
    maybeCreation()
    justCreation()
    futureCreation()
    collectionCreation()
    filterCollection()
    collectionScan()
    collectionCollect()
    collectionBackpressure()
}

/**
 * Using [Single.just] we can create the Single<T>.
 * Once we have the Single we can use all the transformation or filter operators
 * [map, filter]
 * Since only one element is allowed to be emitted, composition operator [flatMap]
 * is not part of the API
 */
private fun singleCreation() {
    Single.just("Hello single reactive world from Kotlin")
        .filter { s -> s.length > 10 }
        .map { s -> upperCaseFunc(s) }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * Using [Maybe] we can guarantee that maybe there's emission, none or an exception once we subscribe and
 * we create the [Disposable]
 */
private fun maybeCreation() {
    Maybe.just("Hello maybe reactive world from Kotlin")
        .filter { s -> s.length > 10 }
        .map { s -> upperCaseFunc(s) }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })

    Maybe.empty<String>()
        .filter { s -> s.length > 10 }
        .map { s -> upperCaseFunc(s) }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * Using [Observable.just] we can create the Observable<T>.
 * Once we have the Observable we can use all the transformation, composition or filter operators
 * [map, flatMap, filter]
 * Once we have the lazy Observable with all the operators to interact with the value,
 * we [Subscribe] transforming the [Observable] to [Disposable] wich is eager so is evaluated.
 * We also configuring two callbacks for the two channels, succeed and error.
 */
private fun justCreation() {
    Observable.just("Hello reactive world from Kotlin")
        .filter { s -> s.length > 10 }
        .map { s -> upperCaseFunc(s) }
        .flatMap { s -> Observable.just("$s!!!!") }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * [fromFuture] operator take a java [Future], and once the future is complete the value
 * in the callback is passed into the [Observable]
 */
private fun futureCreation() {
    Observable.fromFuture(CompletableFuture.supplyAsync { "Hello reactive world from the Future" })
        .map { s -> upperCaseFunc(s) }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * We can create an Observable using a collection, with the sugar extension function [toObservable]
 * which basically the same than [Observable.fromIterable]
 */
private fun collectionCreation() {
    listOf("hello", "?", "reactive", "world", "12", "from", "kotlin").toObservable()
        .filter { s -> s.length > 4 }
        .map { s -> upperCaseFunc(s) }
        .flatMap { s -> Observable.just("$s!!!!") }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * With the same operators we have in all reactive paradigm api we filter using
 * [filter] operator to allow go through values passing a specific predicate function.
 * [takeWhile] operator to run the observable while the predicate condition is passed
 * [take] operator to specify the number of elements you want to pick up.
 */
private fun filterCollection() {
    listOf("hello", "?", "reactive", "world", "12", "from", "kotlin").toObservable()
        .filter { s -> s.length > 4 }
        .takeWhile { s ->
            s.contains("hello") ||
                    s.contains("kotlin") ||
                    s.contains("world")
        }
        .take(1)
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * With Collection we can have a foldable operator, in which we can receive the accumulator type,
 * and each emission of the Observable.
 */
private fun collectionScan() {
    listOf("hello", "reactive", "world").toObservable()
        .scan { acc, next -> "$acc-$next" }
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * [CollectInto] operator allow us to gather all the emission of the Observable, and wrap it up into
 * a collection, that it will be emitted as a single element in the Subscriber.
 */
private fun collectionCollect() {
    listOf("hello", "reactive", "world", "in", "one", "collection").toObservable()
        .map { s -> "_${s}_" }
        .collectInto(mutableListOf<String>(), { list, value -> list.add(value) })
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}

/**
 * Backpressure is an architecture pattern that define two actors, the publisher and consumer,
 * and how the consumer tell the publisher how many items he can handle before overwhelm.
 * In this example, we use [buffer] operator which specify to the Observable, how many items we can
 * process in each emission to the Subscriber channel.
 * Here we specify the Subscriber can handle two items in each emission
 */
private fun collectionBackpressure() {
    listOf("backpressure", "in", "reactive", "world").toObservable()
        .buffer(2)
        .subscribe({ s -> println("Succeed Channel:$s") }, { t -> println("Error channel:$t") })
}