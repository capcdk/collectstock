package business.hashcash

import business.jillion.HikariPoolHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.runBlocking
import org.apache.commons.lang3.RandomStringUtils
import sun.misc.BASE64Encoder
import java.security.MessageDigest
import kotlin.system.measureNanoTime


/**
 * Created by Chendk on 2018/11/29
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
fun main(args: Array<String>) = runBlocking {
    slowHashGroup().consumeEach {
        insertData(it)
    }
}

@ExperimentalCoroutinesApi
fun CoroutineScope.slowHashGroup() = produce {
    var times = 20000
    val group = 5
    while (times <= 5000000) {
        val tripleList = (1..group).map {
            calculate(times)
        }
        println("times:$times, averMeasure:${tripleList.map { it.third.toLong() }.sum() / group}")
        send(tripleList)
        times += 5000
    }
}

fun calculate(times: Int): Triple<String, String, String> {
    val question = RandomStringUtils.randomAlphanumeric(40)
    val digest = MessageDigest.getInstance("SHA-512")
    var result = question.toByteArray()
    val measure = measureNanoTime {
        repeat(times) {
            digest.update(result)
            result = digest.digest()
        }
    } / (1000 * 1000)
    return Triple(question, BASE64Encoder().encodeBuffer(result), measure.toString())
}

fun insertData(questionResultList: List<Triple<String, String, String>>) {
    val insertSql = "INSERT INTO `t_security_hashcash_question` ( `question`,`result`,`measure` ) " +
            " VALUES ( ?,?,? )"
    HikariPoolHolder.getConnection().use {
        val ps = it.prepareStatement(insertSql)
        questionResultList.forEach { pair ->
            ps.setString(1, pair.first)
            ps.setString(2, pair.second)
            ps.setString(3, pair.third)
            ps.addBatch()
        }
        ps.executeBatch()
        it.commit()
    }
}