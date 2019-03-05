package business.ddlconvert

import JsonUtils
import business.jillion.HikariPoolHolder
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset

/**
 * Created by Chendk on 2018/12/7
 */

val unusedTableTitles = listOf("t_goods_library", "t_base_supplier_goods_relation", "t_base_supply_records", "t_employee_goods_recommend_factitious", "t_goods_period_stat", "t_goods_price_set_relation", "t_goods_temporary_column", "t_inv_goods_available_amount", "t_ml_recommend_goods", "t_recommend_goods", "t_report_day_goods_sales", "t_report_month_goods_sales", "t_sales_promotion_free_goods", "t_sales_promotion_goods", "t_sales_promotion_presell_goods", "t_sales_promotion_set_goods")

fun main(args: Array<String>) {
//    groupByLiteApp()
//    groupByEmptyTable()
    groupByTmsWms()
}

fun groupByEmptyTable() {
    val tablesString = FileInputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-all.json")).use {
        it.readBytes()
    }.toString(Charset.forName("UTF-8"))
    val dbTables = JsonUtils.deserialize<List<DBTable>>(tablesString)
    var process = 1
    val isUnusedTablesMap = dbTables!!.groupBy {
        val isEmpty = isEmptyByTableName(it.title)
        println(process++)
        when {
            unusedTableTitles.contains(it.title) || isEmpty -> 1
            else -> 2
        }
    }

    val unusedTables = JsonUtils.serialize(isUnusedTablesMap[1]!!) ?: ""
    val otherTables = JsonUtils.serialize(isUnusedTablesMap[2]!!) ?: ""
//    val errorTables = JsonUtils.serialize(isUnusedTablesMap[0]!!) ?: ""

    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-unused-tables.json")).write(unusedTables.toByteArray())
    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-other-tables.json")).write(otherTables.toByteArray())
//    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-error-tables.json")).write(errorTables.toByteArray())
}

fun isEmptyByTableName(tableName: String): Boolean {
    return HikariPoolHolder.getConnection().use {
        val ps = it.prepareStatement("""
            SELECT * from `$tableName` limit 1
        """.trimIndent())
        val rs = ps.executeQuery()
        !rs.next()
    }
}

fun groupByLiteApp() {
    val tablesString = FileInputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-all.json")).use {
        it.readBytes()
    }.toString(Charset.forName("UTF-8"))
    val dbTables = JsonUtils.deserialize<List<DBTable>>(tablesString)
    val groupedTablesMap = dbTables!!.groupBy { it.title.startsWith("t_lite_app", true) }

    val liteAppTables = JsonUtils.serialize(groupedTablesMap[true]!!) ?: ""
    val otherTables = JsonUtils.serialize(groupedTablesMap[false]!!) ?: ""

    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-lite_app-tables.json")).write(liteAppTables.toByteArray())
    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-other-tables.json")).write(otherTables.toByteArray())
}

fun groupByTmsWms() {
    val tablesString = FileInputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-all.json")).use {
        it.readBytes()
    }.toString(Charset.forName("UTF-8"))
    val dbTables = JsonUtils.deserialize<List<DBTable>>(tablesString)
    val groupedTablesMap = dbTables!!.groupBy {
        it.title.startsWith("t_tms", true) || it.title.startsWith("t_wms", true)
    }

    val tmsWmsTables = JsonUtils.serialize(groupedTablesMap[true]!!) ?: ""
    val otherTables = JsonUtils.serialize(groupedTablesMap[false]!!) ?: ""

    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-tms-wms-tables.json")).write(tmsWmsTables.toByteArray())
    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\laimi-other-tables.json")).write(otherTables.toByteArray())
}