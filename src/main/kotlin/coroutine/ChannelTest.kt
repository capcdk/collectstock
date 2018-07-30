package coroutine

import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking

/**
 * Created by Chendk on 2018/7/27
 */

fun produceSquares() = produce {
    for (x in 1..5) send(x * x)
}

fun main(args: Array<String>) = runBlocking {
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("Done!")
}