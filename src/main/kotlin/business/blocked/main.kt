package business.blocked

import kotlinx.coroutines.*


/**
 * Created by Chendk on 2018/9/19
 */
fun main(args: Array<String>) = runBlocking {
    println("--- main start ---")
    val deferredList = List(10) {
        serviceAsync(it)
    }
    deferredList.parallelStream().forEach {
        runBlocking {
            println("start")
            println("${it.await()} end")
        }
    }
    println("--- main end ---")
}

fun CoroutineScope.serviceAsync(order: Int) = async(start = CoroutineStart.LAZY) {
    blokingIoWork()
    order
}

fun blokingIoWork() = runBlocking {
    //模拟耗时的io操作
    delay(2000)
}
