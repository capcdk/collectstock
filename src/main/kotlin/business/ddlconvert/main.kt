package business.ddlconvert

import business.jillion.HikariPoolHolder

/**
 * Created by Chendk on 2018/12/1
 */

fun main(args: Array<String>) {
    val tables = selectAllTables()
    val fieldTypes = HashSet<String>(100)
    tables.forEach {
        fieldTypes.addTypesByTable(it)
    }
    fieldTypes.map {
        FieldType(it)
    }.run {
        println(JsonUtils.serialize(this))
    }
}

private fun HashSet<String>.addTypesByTable(tableName: String) {
    return HikariPoolHolder.getConnection().use {
        val ps = it.prepareStatement("desc $tableName")
        val resultSet = ps.executeQuery()
        while (resultSet.next()) {
            this.add(resultSet.getString(2).replace(" unsigned", ""))
        }
    }
}

fun selectAllTables(): HashSet<String> {
    return HikariPoolHolder.getConnection().use {
        val ps = it.prepareStatement("show tables")
        val resultSet = ps.executeQuery()
        val tableNames = HashSet<String>(100)
        while (resultSet.next()) {
            tableNames.add(resultSet.getString(1))
        }
        tableNames
    }
}