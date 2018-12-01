import business.jillion.HikariPoolHolder
import business.stockdata.StockSelectorImp1
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sqlbuilder.SqlBuilder
import java.sql.JDBCType
import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong
import kotlin.system.measureNanoTime

/**
 * Created by Chendk on 2018/8/2
 */
fun main(args: Array<String>) = runBlocking {

    measureNanoTime {
        val stockSelector = StockSelectorImp1()

        //获取股票编码、数据标题
        val stockCodeList = stockSelector.getStockCodes()
        val rowNameList = stockSelector.getStockHeaders()

        //建表
        val tableName = createDb(rowNameList)

        val insertCounter = AtomicLong(0)

        val mainJob = launch {
            stockCodeList.forEach {
                launch(Executors.newFixedThreadPool(4).asCoroutineDispatcher()) {
                    val stockData = stockSelector.getStockHistoryData(it)
                    insertData(tableName, rowNameList, stockData).let {
                        println("${Thread.currentThread().name} -- 插入${it}行")
                        insertCounter.addAndGet(it.toLong())
                    }
                }
            }
            println("任务启动循环完成")
        }
        mainJob.join()
    }.let {
        println("总耗时：${it / (1000 * 1000 * 1000)}s")
    }
}

fun createDb(rowNameList: List<String>): String {
    val sqlBuilder = SqlBuilder()
    //定义普通行
    rowNameList.forEach { sqlBuilder.addCommonRow(it, JDBCType.VARCHAR, 50) }
    val tableName = "t_stock_data_until_${LocalDate.now()}"
    //构建建表sql
    val createSql = sqlBuilder.build(tableName)
    HikariPoolHolder.getConnection().use {
        it.createStatement().executeUpdate(createSql)
        it.commit()
    }
    println(createSql)
    return tableName
}

fun insertData(tableName: String, rowNameList: List<String>, stockData: List<Map<String, String>>): Int {
    val insertSql = "INSERT INTO `$tableName` ( ${rowNameList.joinToString(",") { "`$it`" }} ) " +
            " VALUES ( ${rowNameList.joinToString(",") { "?" }} )"
    var insertRow = 0
    HikariPoolHolder.getConnection().use {
        val ps = it.prepareStatement(insertSql)
        val rowNameIdxMap = rowNameList.mapIndexed { rowIdx: Int, rowName: String -> Pair(rowName, rowIdx + 1) }.toMap()
        stockData.forEach {
            it.forEach { rowName, rowData ->
                rowNameIdxMap[rowName]?.let { ps.setString(it, rowData) }
            }
            ps.addBatch()
        }
        insertRow = ps.executeBatch().sum()
        it.commit()
    }
    return insertRow
}