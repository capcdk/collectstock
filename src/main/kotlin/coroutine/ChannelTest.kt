package coroutine

/**
 * Created by Chendk on 2018/7/27
 */

fun main(args: Array<String>) {
    People.Factory("yellow").run {
        this.say()
    }
}

class People private constructor(val alert: String) {

    fun say() {
        println(alert)
    }

    companion object Factory {
        operator fun invoke(color: String): People {
            return when (color.toLowerCase().trim()) {
                "black" -> People("What's up! man")
                "yellow" -> People("你嘎哈")
                "white" -> People("hello,I'm stupid")
                else -> People("")
            }
        }
    }
}

