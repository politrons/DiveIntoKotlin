package main.kotlin

import kotlinx.coroutines.*

suspend fun main() {
    fireAndForgetProcess()
    blockingProcess()
    cancelProcess()
    withTimeoutProcess()
    askPatternProcess()
}

/**
 * Coroutines are light-weight threads. Using [GlobalScope.launch] we're able to run async the process.
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

private fun askPatternProcess() = runBlocking {
    val deferred1: Deferred<Int> = async {
        delay(1000L)
        1000
    }
    val deferred2: Deferred<Int> = async {
        delay(1000L)
        500
    }
    println("The answer is ${deferred1.await() + deferred2.await()}")
}