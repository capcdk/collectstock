import jillion.BatchInsertJillionData
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureNanoTime

/**
 * Created by Chendk on 2018/7/20
 */

//fun main(args: Array<String>) {
//        StockCodeSelector()
//}

fun main(args: Array<String>) {

    val measureList = mutableListOf<Long>()
    repeat(100) {
//        measureList += singleThreadInsert()
        measureList += coroutineInsert()
    }
    println(measureList)
    println("平均耗时 ：${measureList.average()} 毫秒")
}

fun singleThreadInsert(): Long {
    val testInsert = BatchInsertJillionData()
    return measureNanoTime {
        repeat(10) {
            testInsert.execute(1000)
//            println("${Thread.currentThread().name} -- 第 $it 个任务")
        }
    }.let {
        val measure = it / (1000 * 1000)
        println("耗时：${measure}毫秒")
        measure
    }
}

fun coroutineInsert() = runBlocking<Long> {
    val testInsert = BatchInsertJillionData()
    val jobs = mutableListOf<Job>()
    measureNanoTime {
        repeat(10) {
            jobs += launch {
                testInsert.executeAsync(1000)
            }
//            println("${Thread.currentThread().name} -- 第 $it 个任务")
        }
        jobs.forEach { it.join() }
    }.let {
        val measure = it / (1000 * 1000)
        println("耗时：${measure}毫秒")
        measure
    }
}

