package sqlbuilder

import java.sql.JDBCType
import java.text.MessageFormat

/**
 * Created by Chendk on 2018/8/1
 */
class SqlBuilder {

    data class TableRow(val rowSql: String, val rowType: SqlBuilderRow)

    private val rowList = mutableListOf<TableRow>()

    constructor() {
        rowList += DEFAULT_ID_ROW
        rowList += DEFAULT_ID_KEY
    }

    constructor(tableId: String) {
        rowList += SqlBuilderRow.ID_ROW.build(tableId)
        rowList += SqlBuilderRow.ID_KEY.build(tableId)
    }

    fun addCommonRow(rowName: String, jdbcType: JDBCType, dataSize: Int, qualifier: Array<Qualifier>) {
        rowList += SqlBuilderRow.COMMON_ROW.build(rowName, jdbcType.name, dataSize.toString(), qualifier.joinToString(" "))
    }

    fun addCommonKey(rowName: String, keyName: String, keyType: KeyType) {
        rowList += SqlBuilderRow.COMMON_KEY.build(keyName, rowName, keyType.type)
    }

    companion object {

        const val DDL_CREATE_TABLE_ORIGIN = "CREATE TABLE `{0}` ( {1} ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;"

        val DEFAULT_ID_ROW = SqlBuilderRow.ID_ROW.build("id")
        val DEFAULT_ID_KEY = SqlBuilderRow.ID_KEY.build("id")

        private fun SqlBuilderRow.build(vararg args: String) = this.let { TableRow(MessageFormat.format(it.originSql, args), it) }

    }

}