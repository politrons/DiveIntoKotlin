package main.kotlin

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

/**
 * Coroutines are light-weight threads. All blocking code must be executed inside [runBlocking]
 * which it will be executed in a coroutine and it will make the join with the main thread once
 * the process finish.
 */
@ObsoleteCoroutinesApi
fun main() {
    fireAndForgetProcess()
    runBlocking { blockingProcess() }
    runBlocking { cancelProcess() }
    runBlocking { withTimeoutProcess() }
    runBlocking { askPatternProcess() }
    runBlocking { askLazyPatternProcess() }
    composeDeferred()
    renderFunction()
}

/**
 * Using [GlobalScope.launch] we're able to run async the process.
 * This operation it will create a [Job] instance which we can use to perform several operations.
 * When we print the state of the job in this case is [Active]
 */
private fun fireAndForgetProcess() {
    val job: Job = GlobalScope.launch {
        delay(500L)
        println("Async World! in ${Thread.currentThread().name}") // print after delay
    }
    println(job)
    println("Hello,")
    Thread.sleep(1000L)
}

/**
 * In case we want to wait and blocking for a process to finish we just need to use [join] operator from Job in the
 * main thread.
 * When we print the state of the job in this case is [Completed]
 * Kotlin only can run this blocking process from [runBlocking] which only it's recommended from main(init-end)
 * in case we don't run in a runBlocking we need to specify the function with [suspend] which means is not evaluated
 *  and join into the main thread until is running in a [runBlocking]
 */
private suspend fun blockingProcess() {
    val job: Job = GlobalScope.launch {
        delay(500L)
        println("Blocking World! in ${Thread.currentThread().name}") // print after delay
    }
    job.join()
    println(job)
    println("Finally you finish!") // main thread continues while coroutine is delayed
}

/**
 * In case we want to cancel a light-weight coroutine we just need to ise [cancel] operator of job.
 * When we print the state of the job in this case is [Cancelling/Cancelled] depending if the process of cancel ends already.
 */
private suspend fun cancelProcess() {
    val job: Job = GlobalScope.launch {
        delay(500L)
        println("Blocking World! in ${Thread.currentThread().name}") // print after delay
    }
    job.cancel()
    println(job)
    println("I can't wait no more Finally you finish!") // main thread continues while coroutine is delayed
}

/**
 * If you need to establish a timeout for a coroutine, you just need to wrap the process in [withTimeout] specifying
 * the max time to finish the process
 */
private suspend fun withTimeoutProcess() {
    val job: Job = GlobalScope.launch {
        withTimeout(500) {
            println("I'm sleeping")
            delay(1000L)
        }
    }
    println("waiting.......") // main thread continues while coroutine is delayed
    job.join()
    println(job)
}

/**
 * In Kotlin to perform ask pattern and run a process and receive a future, here called [Deferred] with
 * the future response type, we use operator [async]
 * Once we have this Deferred type we need to use [await] to wait and obtain the value in the main Thread
 * All the concurrent process can specify in which dispatcher(pool of threads) being executed. We just have to pass as [context]
 * a [ExecutorCoroutineDispatcher] implementation.
 */
@ObsoleteCoroutinesApi
private suspend fun askPatternProcess() {
    val deferred: Deferred<Int> = GlobalScope.async {
        delay(1000L)
        println("Running async process in thread ${Thread.currentThread().name}")
        1000
    }
    println("The answer is ${deferred.await()}")
}

/**
 * In case we want to create a computation to be executed in another thread but not
 * in the moment of the creation, just like promises we need to just define the async with
 * [start=CoroutineStart.LAZY] then only when we run the operator [start] is when
 * the execution start. Just like promise.future -> future.succeed/failure
 */
private suspend fun askLazyPatternProcess() {
    val lazyDeferred: Deferred<Int> = GlobalScope.async(start = CoroutineStart.LAZY) {
        delay(1000L)
        1000
    }
    println("State $lazyDeferred")
    lazyDeferred.start()
    println("State $lazyDeferred")
    println("The answer is ${lazyDeferred.await()}")
}

/**
 * Since all the blocking code [runBlocking] is running in a Coroutine, and we're not blocking the OS Thread
 * it's perfectly fine to make composition to [await] for the first deferred inside the second one, so
 * instead of use of [flatMap we can just wait until that happen to finish the second deferred.]
 */
@ObsoleteCoroutinesApi
private fun composeDeferred() = runBlocking(context = Dispatchers.Default) {
    val deferred1: Deferred<Int> = async {
        delay(1000L)
        println("Running async process in thread ${Thread.currentThread().name}")
        1000
    }
    val deferred2: Deferred<Int> = async(context = newFixedThreadPoolContext(100, "MyThreadPool")) {
        delay(1000L)
        println("Running async process with composition of value ${deferred1.await()} in thread ${Thread.currentThread().name}")
        500
    }
    println("Final result  ${deferred2.await()}")
}

/**
 * This function emulate a entry server request and how we manage to invoke several async service and render in
 * async way
 */
private fun renderFunction() {
    val channel: Channel<String> = Channel(1000)

    GlobalScope.launch(context = newFixedThreadPoolContext(30, "MyDispatcher")) {
        coroutineScope {
            for (request in channel) {
                val result = "Hello ${asyncService1(request)}"
                println("Rendering value: $result")
            }
        }

    }
    GlobalScope.launch { channel.send("!!!!") }
    GlobalScope.launch { channel.send("!!!!") }//They can run in parallel
    Thread.sleep(2000) // Only to prove that works!
}

private suspend fun asyncService1(value: String): String {
    delay(100L)
    val result = asyncService2(value)
    return "Async $result"
}

/**
 * Once we have a function that is blocking we need to wrap it in a coroutine, and all the functions
 * that it will invoke this function they will have to be suspend or wrap the invocation into [runBlocking]
 * which it will block the OS thread, try to avoid that at all cost!
 */
private suspend fun asyncService2(value: String): String {
    return withContext(Dispatchers.Default) {
        delay(100L)
        "World $value"
    }
}


