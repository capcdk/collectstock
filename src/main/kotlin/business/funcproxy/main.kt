package business.funcproxy

/**
 * Created by Chendk on 2018/9/19
 */

fun main(args: Array<String>) {
    val printInfoProxy = ExampleProxy(A()::printInfo)
    printInfoProxy("abc")
    printInfoProxy.abc()
}


class A {
    fun printInfo(s: String) {
        println("i'm $s")
    }
}


class ExampleProxy(private val method: (String) -> Unit) : (String) -> Unit {
    override fun invoke(param: String) {
        println("proxy do something...")
        method(param)
    }

    fun abc() {
        println("abc")
    }
}
