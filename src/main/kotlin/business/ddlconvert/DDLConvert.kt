package business.ddlconvert

import JsonUtils
import business.jillion.HikariPoolHolder
import java.io.File
import java.io.FileOutputStream
import java.util.*

/**
 * Created by Chendk on 2018/12/1
 */

fun main(args: Array<String>) {
    val schema = "laimi_er"
    findFieldTypes(schema)
    buildTableEntities(schema)
}

fun buildTableEntities(schema: String): List<DBTable> {
    val dbTables = listTables(schema)
    dbTables.forEach {
        it.fields = listTableFields(schema, it.title)
        it.indexs = listTableIndexs(it.title)
    }
    val dbTablesString = JsonUtils.serialize(dbTables) ?: ""
    FileOutputStream(File("C:\\Users\\Capc\\Desktop\\PDM\\PDMan\\$schema.json")).write(dbTablesString.toByteArray())
    return dbTables
}

fun findFieldTypes(schema: String) {
    listFieldTypes(schema)
            .map {
                FieldType(it)
            }.run {
                println(JsonUtils.serialize(this))
            }
}

private fun listFieldTypes(schema: String): HashSet<String> {
    return HikariPoolHolder.getConnection().use {
        val types = HashSet<String>(500)
        val ps = it.prepareStatement("""
            select *
            from information_schema.columns
            where table_schema = '$schema'
        """.trimIndent())
        val resultSet = ps.executeQuery()
        while (resultSet.next()) {
            types.add(resultSet.getString("COLUMN_TYPE"))
        }
        types
    }
}

private fun listTables(schema: String): List<DBTable> {
    return HikariPoolHolder.getConnection().use {
        val tables = LinkedList<DBTable>()
        val ps = it.prepareStatement("""
            select *
            from information_schema.tables
            where table_schema = '$schema'
        """.trimIndent())
        val resultSet = ps.executeQuery()
        while (resultSet.next()) {
            val tableComment = resultSet.getString("TABLE_COMMENT")
            val tableName = resultSet.getString("TABLE_NAME")
            val schemaName = resultSet.getString("TABLE_SCHEMA")
            tables.add(DBTable(tableName, remark = tableComment, schema = schemaName))
        }
        tables
    }
}

private fun listTableFields(schema: String, table: String): List<DBField> {
    return HikariPoolHolder.getConnection().use {
        val fields = LinkedList<DBField>()
        val resultSet = it.prepareStatement("""
            select *
            from information_schema.columns
            where table_schema = '$schema'
            and table_name = '$table'
        """.trimIndent()).executeQuery()
        while (resultSet.next()) {
            val columnName = resultSet.getString("COLUMN_NAME")
            val columnType = resultSet.getString("COLUMN_TYPE")
            val columnDefault = resultSet.getString("COLUMN_DEFAULT")
            val columnNotNull = resultSet.getString("IS_NULLABLE") == "NO"
            val isAutoIncrement = resultSet.getString("EXTRA") == "auto_increment"
            val columnComment = resultSet.getString("COLUMN_COMMENT")
            val pk = resultSet.getString("COLUMN_KEY") == "PRI"

            val dbField = DBField(
                    name = columnName,
                    chnname = columnName,
                    type = columnType,
                    pk = pk,
                    defaultValue = columnDefault,
                    notNull = columnNotNull,
                    autoIncrement = isAutoIncrement,
                    remark = columnComment
            )
            fields.add(dbField)
        }
        fields
    }
}

private fun listTableIndexs(table: String): List<DBIndex> {
    return HikariPoolHolder.getConnection().use {
        val indexsMap = mutableMapOf<String, DBIndex>()
        val resultSet = it.prepareStatement("show index from $table").executeQuery()
        while (resultSet.next()) {
            val unique = resultSet.getInt("Non_unique") == 0
            val indexName = resultSet.getString("Key_name")
            val indexColumn = resultSet.getString("Column_name")

            val preIndex = indexsMap[indexName]
            if (preIndex != null) {
                preIndex.fields.add(indexColumn)
            } else {
                indexsMap[indexName] = DBIndex(indexName, unique, hashSetOf(indexColumn))
            }
        }
        indexsMap.values.toList()
    }
}