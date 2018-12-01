package business.increaseRead

import business.stockdata.SingletonJsoup
import kotlinx.coroutines.runBlocking

/**
 * Created by Chendk on 2018/9/19
 */

fun main(args: Array<String>) = runBlocking {

    repeat(10) {
        SingletonJsoup.randomDelaySoup("https://www.jianshu.com/p/7497bce754eb", "www.jianshu.com")
    }
}