package main.kotlin.monads

fun main() {
    val either: Either<Throwable, String> = Right("hello either")
    println(when (either) {
        is Right -> either.value
        is Left -> "Error caused by ${either.value}"
    })
}

sealed class Either<L, R> {}

class Right<L, R>(val value: R) : Either<L, R>() {
    fun <B> map(func: (R) -> B): Either<L, B> {
        return Right(func(value))
    }

    fun <B> flatMap(func: (R) -> Either<L, B>): Either<L, B> {
        TODO("Not yet implemented")
    }

    fun filter(func: (R) -> Boolean): Either<L, R> {
        TODO("Not yet implemented")
    }

    fun <B> fold(ifEmpty: B, func: (R) -> B): Either<L, B> {
        TODO("Not yet implemented")
    }

    fun <B> foldLeft(v: B, func: (B, R) -> B): Either<L, B> {
        TODO("Not yet implemented")
    }

    fun <B> foldRight(v: B, func: (R, B) -> B): Either<L, B> {
        TODO("Not yet implemented")
    }

}

class Left<L, R>(val value: L) : Either<L, R>() {
    fun <B> map(func: (L) -> B): Either<B, R> {
        return Left(func(value))
    }

    fun <B> flatMap(func: (R) -> Option<B>): Either<L, B> {
        TODO("Not yet implemented")
    }

    fun filter(func: (R) -> Boolean): Either<L, R> {
        TODO("Not yet implemented")
    }

    fun <B> fold(ifEmpty: B, func: (R) -> B): Either<L, B> {
        TODO("Not yet implemented")
    }

    fun <B> foldLeft(v: B, func: (B, R) -> B): Either<L, B> {
        TODO("Not yet implemented")
    }

    fun <B> foldRight(v: B, func: (R, B) -> B): Either<L, B> {
        TODO("Not yet implemented")
    }

}