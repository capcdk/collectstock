package business.iterateDir

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.toList
import kotlin.system.measureNanoTime

/**
 * Created by Chendk on 2019/1/15
 */

fun main(args: Array<String>) {
//    val indexUrl = collectDirOrFile("D:\\GitProject\\E-R", "indexFile.txt")
    findTableNotExisted("D:\\GitProject\\E-R\\indexFile.txt", "C:\\Users\\Capc\\Desktop\\PDM\\ALL")
//    testRegex()
}

fun String.underLineToCamel(removePrefix: String? = null): String {
    val tmp = removePrefix?.let { this.removePrefix(removePrefix) } ?: this
    return tmp.split('_').joinToString("") { it.capitalize() }
}

fun testRegex() {
    val tableName = "t_account"
    val fileSystem = FileSystems.getDefault()

    val stringBuilder = StringBuilder()
    val allIndexLines = Files.newBufferedReader(fileSystem.getPath("D:\\GitProject\\E-R\\indexFile.txt")).use {
        it.lines()
                .filter { line -> line.isNotBlank() }
                .peek { line -> stringBuilder.append(line).append(System.lineSeparator()) }
                .toList()
    }
    val allIndexString = stringBuilder.toString()
    val tableRegex = Regex("\\b$tableName\\b")

//    // 预热
//    (1..5).forEach {
//        tableRegex.matchEntire(allIndexString)
//        tableRegex.find(allIndexString)
//
//        allIndexLines.forEach { indexLine ->
//            tableRegex.matchEntire(indexLine)
//            tableRegex.find(indexLine)
//        }
//    }


    // 测试
    println("耗时: ${measureNanoTime {
        println("单string matchEntire: " + tableRegex.matchEntire(allIndexString)?.groupValues?.size)
    } / 1000 / 1000} ms")

    println("耗时: ${measureNanoTime {
        println("单string findAll: " + tableRegex.findAll(allIndexString).sumBy { it.groupValues.size })
    } / 1000 / 1000} ms")

    println("耗时: ${measureNanoTime {
        println("行队列 matchEntire: " + allIndexLines.sumBy { tableRegex.matchEntire(it)?.groupValues?.size ?: 0 })
    } / 1000 / 1000} ms")

    println("耗时: ${measureNanoTime {
        println("行队列 findAll: " + allIndexLines.sumBy { tableRegex.findAll(it).sumBy { it.groupValues.size } })
    } / 1000 / 1000} ms")
}

fun findTableNotExisted(indexUrl: String, tableRootUrl: String) {
    val fileSystem = FileSystems.getDefault()

    val allIndexLines = Files.newBufferedReader(fileSystem.getPath(indexUrl)).use {
        it.lines().filter { line -> line.isNotBlank() }.toList()
    }

    var executeTotal = 0
    Files.newBufferedWriter(fileSystem.getPath("$tableRootUrl\\废弃表(代码未引用)统计.txt")).use { writer ->
        Files.walkFileTree(fileSystem.getPath(tableRootUrl), object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                val fileName = file.fileName.toString()
                if (fileName.contains(".txt")) {
                    return FileVisitResult.CONTINUE
                }

                writer.write("$fileName 统计情况（原名匹配数，大写驼峰匹配数）：")
                writer.newLine()
                Files.newBufferedReader(file, Charsets.UTF_8).lines().parallel().forEach { tableName ->
                    executeTotal++
                    println("========>总数$executeTotal 处理$tableName")
                    val upperCamelTableName = tableName.underLineToCamel("t_")

                    val tableRegex = Regex("\\b$tableName\\b")
                    val upperCamelTableRegex = Regex("\\b$upperCamelTableName\\b")

                    val tableMatchCnt = AtomicInteger(0)
                    val upperCamelMatchCnt = AtomicInteger(0)

                    allIndexLines.parallelStream().forEach { tableMatchCnt.addAndGet(tableRegex.findAll(it).sumBy { result -> result.groupValues.size }) }
                    allIndexLines.parallelStream().forEach { upperCamelMatchCnt.addAndGet(upperCamelTableRegex.findAll(it).sumBy { result -> result.groupValues.size }) }

                    if (tableMatchCnt.get() <= 0) {
                        writer.write("$tableName ${upperCamelMatchCnt.get().takeIf { it > 0 }?.let { "($upperCamelTableName: $it)" }
                                ?: ""}${System.lineSeparator()}")
                    }
                }

                writer.newLine()
                writer.newLine()
                writer.newLine()
                return FileVisitResult.CONTINUE
            }

            override fun visitFileFailed(file: Path, exc: IOException?): FileVisitResult {
                println("======> 读取文件失败：$file")
                return FileVisitResult.CONTINUE
            }
        })
    }

}

/**
 * 深度遍历所有文件，并聚合到单一文件中
 * [rootUrl] 遍历文件的根目录
 */
fun collectDirOrFile(rootUrl: String, indexFileName: String): String {
    val fileSystem = FileSystems.getDefault()

    val ignoreDirs = listOf(".idea", ".git", "target", ".mvn", "logs")
    val ignoreFiles = listOf(".iml", "indexFile", ".md", ".gitignore", ".xlsx", "GeneratorService", "mvnw")

    val errorPath = mutableListOf<String>()
    val ignorePath = mutableListOf<String>()
    var dirCnt = 0
    var fileCnt = 0

    val indexUrl = "$rootUrl\\$indexFileName"
    Files.newBufferedWriter(fileSystem.getPath(indexUrl), Charsets.UTF_8).use { writer ->

        Files.walkFileTree(fileSystem.getPath(rootUrl), object : SimpleFileVisitor<Path>() {

            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes?): FileVisitResult {
                dirCnt++
                val dirName = dir.fileName.toString()
                return if (ignoreDirs.any { dirName == it }) {
                    ignorePath.add(dir.toString())
                    FileVisitResult.SKIP_SUBTREE
                } else {
                    FileVisitResult.CONTINUE
                }
            }

            /**
             * 访问文件
             */
            override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
                fileCnt++

                if (ignoreFiles.any { file.fileName.toString().contains(it) }) {
                    ignorePath.add(file.toString())
                    return FileVisitResult.CONTINUE
                }

                try {
                    Files.newBufferedReader(file, Charsets.UTF_8).lines().forEach { line ->
                        writer.write(line)
                        writer.newLine()
                    }
                } catch (e: Exception) {
                    errorPath.add(file.toString())
                    return FileVisitResult.CONTINUE
                }

                println("文件转储: $fileCnt, $file")
                return FileVisitResult.CONTINUE
            }

            /**
             * 访问文件失败
             */
            override fun visitFileFailed(file: Path?, exc: IOException?): FileVisitResult {
                errorPath.add(file.toString())
                return FileVisitResult.CONTINUE
            }
        })
    }

    println("=========> 无法访问的路径：")
    errorPath.forEach { println(it) }
    println()

    println("=========> 忽略的文件or目录：")
    ignorePath.forEach { println(it) }
    println()

    println("""
        统计：
            总遍历文件夹数：$dirCnt
            总遍历文件数：$fileCnt

            无法访问的路径数：${errorPath.size}
            忽略的文件or目录：${ignorePath.size}
    """.trimIndent())

    return indexUrl
}



