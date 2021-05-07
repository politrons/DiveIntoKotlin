package main.kotlin


/**
 * Normally in JVM when we use lambdas it's an anonymous class of interface FunctionN which means when
 * is generated the bytecode, it replace the lambda by the class.
 * The code you see below it looks like:
 *
 * public static final void nonInlineFunction(Int value,Function1 func) {
        func.invoke(value)
    }

 * And the invoker of the function it creates a new instance of Function1 in each iteration
 *
    nonInlineFunction(1, new Function1() {
        override fun invoke(n: Int) {
            1981 * n
        }
    })
 *
 * Benchmark result:
 *
 * NonInline time for 1000000000 iteration function execution -> 403 ms
 * Inline time for 1000000000 iteration function execution -> 1205 ms
 */
fun main() {
    var time = System.currentTimeMillis()
    for (i in 1..1000000000) {
        inlineFunction(i) { n -> 1981 * n }
    }
    println("Inline time: ${System.currentTimeMillis() - time}")
    time = System.currentTimeMillis()
    for (i in 1..1000000000) {
        nonInlineFunction(i) { n -> 1981 * n }
    }
    println("NonInline time: ${System.currentTimeMillis() - time}")
}

/**
 * Here since the function is not [inline] and we have a higher order function, the
 * JVM it will create in runtime a new instance of Function1 each time the function is
 * invoked.
 */
fun nonInlineFunction(value: Int, func: (Int) -> Int) {
    func(value)
}

/**
 * The higher order function that hold in the body of this function it's invoke directly in
 * the CallSite, and the instance of the Function1 is only created once, and not created
 * each time we invoke the function.
 */
inline fun inlineFunction(value: Int, func: (Int) -> Int) {
    func(value)
}