package business.ddlconvert

import java.util.*

/**
 * Created by Chendk on 2018/12/3
 */
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    while (true) {
        val input = scanner.nextLine()
        println(input.replaceFirst("t_", "").toUpperCamel())
    }
}

fun String.toUpperCamel(removePrefix: String? = null): String {
    val input =
            if (removePrefix != null)
                this.removePrefix(removePrefix)
            else
                this

    val chars = input.toMutableList()
    var temp = 0
    (0 until chars.size).forEach {
        val i = it - temp
        val char = chars[i]
        if (char == '_') {
            chars.removeAt(i)
            chars[i] = chars[i].toUpperCase()
            temp++
        }
    }
    chars[0] = chars[0].toUpperCase()
    return String(chars.toCharArray())
}