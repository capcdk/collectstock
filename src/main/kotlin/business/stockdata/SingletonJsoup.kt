package business.stockdata

import kotlinx.coroutines.experimental.delay
import org.apache.commons.lang3.RandomUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.concurrent.TimeUnit

/**
 * Created by Chendk on 2018/8/3
 */
object SingletonJsoup {

    val userAgentList = listOf(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100202 Firefox/62.0"
    )

    /**
     * 当前延迟，ms
     */
    var currentDelay: Long = 300L
    /**
     * 上次请求时间戳，ns（10^6 * ms）
     */
    var lastRequestTimestamp: Long = System.nanoTime()

    /**
     * 利用指定的Header链接到URL ,并拉取资源
     */
    fun soup(url: String, host: String): Document {

        return Jsoup.connect(url)
                .userAgent(userAgentList[RandomUtils.nextInt(0, userAgentList.size - 1)])
                .headers(mapOf(
                        "Host" to host,
                        "Accept" to "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8",
                        "Accept-Encoding" to "gzip, deflate",
                        "Accept-Language" to "zh-CN,zh;q=0.9",
                        "Cache-Control" to "max-age=0",
                        "Connection" to "keep-alive",
                        "Upgrade-Insecure-Requests" to "1"
                )).get()
    }

    /**
     * soup方法随机延迟版
     */
    suspend fun randomDelaySoup(url: String, host: String): Document {

        while ((lastRequestTimestamp + (currentDelay * 1000 * 1000)) > System.nanoTime()) {
            delay((lastRequestTimestamp + (currentDelay * 1000 * 1000)) - System.nanoTime(), TimeUnit.NANOSECONDS)
        }
        //到达延迟，更新当前延迟（300ms~2s 随机）
        lastRequestTimestamp = System.nanoTime()
        currentDelay = RandomUtils.nextLong(300, 2000)
        println("${Thread.currentThread().name} -- ${System.currentTimeMillis()} -- 获取数据")
        return soup(url, host)
    }

}