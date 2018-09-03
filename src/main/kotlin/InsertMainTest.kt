import business.jillion.BatchInsertJillionData
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.system.measureNanoTime

/**
 * Created by Chendk on 2018/7/20
 */



fun main(args: Array<String>) {

    val measureList = mutableListOf<Long>()
    repeat(10) {
//        measureList += singleThreadInsert(100, 10000)
        measureList += coroutineInsert(10000,10)
    }
    println(measureList)
    println("平均耗时 ：${measureList.average()} 毫秒")
}

fun singleThreadInsert(missionCount: Int, rowsPerMission: Int): Long {
    val testInsert = BatchInsertJillionData()
    return measureNanoTime {
        repeat(missionCount) {
            testInsert.execute(rowsPerMission)
//            println("${Thread.currentThread().name} -- 第 $it 个任务")
        }
    }.let {
        val measure = it / (1000 * 1000)
        println("耗时：${measure}毫秒，速率：${testInsert.counter.get() / measure} rows/ms")
        measure
    }
}

fun coroutineInsert(missionCount: Int, rowsPerMission: Int) = runBlocking {
    val testInsert = BatchInsertJillionData()
    val jobs = mutableListOf<Job>()
    measureNanoTime {
        repeat(missionCount) {
            jobs += launch {
                testInsert.executeAsync(rowsPerMission)
            }
//            println("${Thread.currentThread().name} -- 第 $it 个任务")
        }
        jobs.forEach { it.join() }
    }.let {
        val measure = it / (1000 * 1000)
        println("耗时：${measure}毫秒，速率：${testInsert.counter.get() / measure} rows/ms")
        measure
    }
}

