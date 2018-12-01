package business.jillion

import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.RandomStringUtils
import org.apache.commons.lang3.RandomUtils
import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalTime
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by Chendk on 2018/7/30
 */

class BatchInsertJillionData {

    private var sql = "INSERT INTO test_jillion_batch_insert (`str1`,`str2`,`time1`,`time2`,`int1`,`int2`) VALUES ( ? , ? , ? , ? , ? , ? )"

    val counter = AtomicInteger(0)
    private val currTS = System.currentTimeMillis()

    fun execute(insertRows: Int) = runBlocking {
        val connection = HikariPoolHolder.getConnection()
        val ps = connection.prepareStatement(sql)
        for (i in 1..insertRows) {
            buildSql(ps)
            ps.addBatch()
        }
        ps.executeBatch().sum().let { counter.addAndGet(insertRows) }
        connection.commit()
//        delay(200)
        connection.close()
        println("${Thread.currentThread().name} -- ${LocalTime.now()} -- 成功插入${counter.get()}条")
    }

    fun executeAsync(insertRows: Int) {
        val connection = HikariPoolHolder.getConnection()
        val ps = connection.prepareStatement(sql)
        for (i in 1..insertRows) {
            buildSql(ps)
            ps.addBatch()
        }
        ps.executeBatch().sum().let { counter.addAndGet(insertRows) }
        connection.commit()
//        delay(200)
        connection.close()
        println("${Thread.currentThread().name} -- ${LocalTime.now()} -- 成功插入${counter.get()}条")
    }

    private fun buildSql(ps: PreparedStatement) {
        ps.setString(1, RandomStringUtils.randomAlphabetic(60))
        ps.setString(2, RandomStringUtils.randomAlphabetic(60))
        ps.setTimestamp(3, Timestamp(currTS - RandomUtils.nextLong(0, 1000000000000)))
        ps.setTimestamp(4, Timestamp(currTS - RandomUtils.nextLong(0, 1000000000000)))
        ps.setLong(5, RandomUtils.nextLong(0, 10000000))
        ps.setLong(6, RandomUtils.nextLong(0, 10000000))
    }


}