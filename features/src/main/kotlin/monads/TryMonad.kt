//package main.kotlin.monads
//
//fun main() {
//    val success: Try<String> = Try.of { -> invokeFunc() }
//    println(when (success) {
//        is Success -> success.value
//        is Failure -> "Error caused by ${success.throwable}"
//    }
//    )
//}
//
//fun invokeFunc(): String {
//    return "hello try"
//}
//
//sealed class Try<T> {
//    companion object {
//        fun <T> of(action: () -> T): Try<T> {
//            return when (val runCatching: Result<T> = runCatching(action)) {
//                is Throwable -> Failure(runCatching.exceptionOrNull()) as Try<T>
//                else -> Success(runCatching.getOrNull()) as Try<T>
//            }
//        }
//    }
//}
//
//class Success<T>(val value: T) : Try<T>() {
//    fun <B> map(func: (T) -> B): Try<B> {
//        return when (val runCatching = kotlin.runCatching { func(value) }) {
//            is Throwable -> Failure(1)
//            else -> Success(runCatching.getOrThrow())
//        }
//    }
//
//    fun <B> flatMap(func: (T) -> Option<B>): Try<B> {
//        TODO("Not yet implemented")
//    }
//
//    fun filter(func: (T) -> Boolean): Try<T> {
//        TODO("Not yet implemented")
//    }
//
//    fun <B> fold(ifEmpty: B, func: (T) -> B): Try<B> {
//        TODO("Not yet implemented")
//    }
//
//    fun <B> foldLeft(v: B, func: (B, T) -> B): Try<B> {
//        TODO("Not yet implemented")
//    }
//
//    fun <B> foldRight(v: B, func: (T, B) -> B): Try<B> {
//        TODO("Not yet implemented")
//    }
//
//}
//
//class Failure<T>(val throwable: T) : Try<T>() {
//
//    override fun <B> map(func: (Throwable?) -> B): Try<T> {
//        return Failure(throwable)
//    }
//
//    override fun <B> flatMap(func: (Throwable?) -> Option<B>): Try<Throwable?> {
//        return Failure(throwable)
//    }
//
//    override fun filter(func: (Throwable?) -> Boolean): Try<Throwable?> {
//        return Failure(throwable)
//    }
//
//    override fun <B> fold(ifEmpty: B, func: (Throwable?) -> B): Try<Throwable?> {
//        return Failure(throwable)
//    }
//
//    override fun <B> foldLeft(v: B, func: (B, Throwable?) -> B): Try<Throwable?> {
//        return Failure(throwable)
//    }
//
//    override fun <B> foldRight(v: B, func: (Throwable?, B) -> B): Try<Throwable?> {
//        return Failure(throwable)
//    }
//
//}