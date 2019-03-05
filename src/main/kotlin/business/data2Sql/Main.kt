package business.data2Sql

import JsonUtils
import business.jillion.HikariPoolHolder
import toPlainString
import java.nio.charset.Charset
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.*

/**
 * Created by Chendk on 2019/1/12
 */

fun main(args: Array<String>) {
    val allCouponPlanCons = allCouponPlanCon()
    val allCouponPlanRecords = allCouponPlanRecord()
    val allCouponPlanUserRels = allCouponPlanUserRel()

    val sql1 = "INSERT INTO `t_coupon_plan_condition` ( `plan_id`, `condition_json`, `created_by`, `creation_date`, `last_updated_by`, `last_update_date` ) " +
            " VALUES ${allCouponPlanCons.joinToString { singleDataSql(it)!! }} ;"
    val sql2 = "INSERT INTO `t_coupon_plan_record` ( `plan_id`, `oper_name`, `content`, `created_by`, `creation_date`, `last_updated_by`, `last_update_date` ) " +
            " VALUES ${allCouponPlanRecords.joinToString { singleDataSql(it)!! }} ;"
    val sql3 = "INSERT INTO `t_coupon_plan_user_rel` ( `plan_id`, `relation_json` ) " +
            " VALUES ${allCouponPlanUserRels.joinToString { singleDataSql(it)!! }} ;"

    val rootPath = "C:\\Users\\Capc\\Desktop\\上线脚本\\合仓\\mongo\\"
    val utf8 = Charset.forName("UTF-8")

    Files.newBufferedWriter(FileSystems.getDefault().getPath(rootPath + "laimi_activity库 - 插入mongo拜访赠券数据.sql"), utf8).use {
        it.write(sql1)
        it.newLine()
        it.write(sql2)
        it.newLine()
        it.write(sql3)
    }

}

fun singleDataSql(data: Any): String? {
    return when (data) {
        is CouponPlanCon -> {
            "(${data.planId}, '${JsonUtils.serialize(data.conditionJson)}', ${data.createdBy}, '${Date(data.creationDate!!).toPlainString()}', ${data.lastUpdatedBy}, '${Date(data.lastUpdateDate!!).toPlainString()}')"
        }
        is CouponPlanUserRel -> {
            "(${data.planId}, '${JsonUtils.serialize(data)}')"
        }
        is CouponPlanRecord -> {
            "(${data.planId}, '${data.operName}', '${data.content}', ${data.createdBy}, '${Date(data.creationDate!!).toPlainString()}', ${data.lastUpdatedBy}, '${Date(data.lastUpdateDate!!).toPlainString()}')"
        }
        else -> null
    }
}

private fun allCouponPlanCon(): List<CouponPlanCon> {
    return HikariPoolHolder.getConnection().use {
        val tableResults = LinkedList<CouponPlanCon>()
        val ps = it.prepareStatement("""
            select *
            from couponPlanCon
        """.trimIndent())
        val resultSet = ps.executeQuery()
        while (resultSet.next()) {
            tableResults.add(CouponPlanCon(
                    resultSet.getLong("planId"),
                    resultSet.getLong("createdBy"),
                    resultSet.getLong("lastUpdatedBy"),
                    resultSet.getLong("creationDate"),
                    resultSet.getLong("lastUpdateDate"),
                    ConditionJson(
                            resultSet.getString("storeCon"),
                            resultSet.getString("loginCon"),
                            resultSet.getString("orderCon")
                    )
            ))
        }
        tableResults
    }
}

private fun allCouponPlanRecord(): List<CouponPlanRecord> {
    return HikariPoolHolder.getConnection().use {
        val tableResults = LinkedList<CouponPlanRecord>()
        val ps = it.prepareStatement("""
            select *
            from couponPlanRecord
        """.trimIndent())
        val resultSet = ps.executeQuery()
        while (resultSet.next()) {
            tableResults.add(CouponPlanRecord(
                    resultSet.getLong("planId"),
                    resultSet.getString("content"),
                    resultSet.getString("operName"),
                    resultSet.getLong("createdBy"),
                    resultSet.getLong("lastUpdatedBy"),
                    resultSet.getLong("creationDate"),
                    resultSet.getLong("lastUpdateDate")
            ))
        }
        tableResults
    }
}

private fun allCouponPlanUserRel(): List<CouponPlanUserRel> {
    return HikariPoolHolder.getConnection().use {
        val tableResults = LinkedList<CouponPlanUserRel>()
        val ps = it.prepareStatement("""
            select *
            from couponPlanUserRel
        """.trimIndent())
        val resultSet = ps.executeQuery()
        while (resultSet.next()) {
            tableResults.add(CouponPlanUserRel(
                    resultSet.getLong("_id"),
                    resultSet.getString("relVoList")
            ))
        }
        tableResults
    }
}

data class CouponPlanCon(
        val planId: Long? = null,
        val createdBy: Long? = null,
        val lastUpdatedBy: Long? = null,
        val creationDate: Long? = null,
        val lastUpdateDate: Long? = null,
        val conditionJson: ConditionJson
)

data class ConditionJson(
        val storeCon: String? = null,
        val loginCon: String? = null,
        val orderCon: String? = null
)

data class CouponPlanRecord(
        val planId: Long? = null,
        val content: String? = null,
        val operName: String? = null,
        val createdBy: Long? = null,
        val lastUpdatedBy: Long? = null,
        val creationDate: Long? = null,
        val lastUpdateDate: Long? = null
)

data class CouponPlanUserRel(
        val planId: Long? = null,
        val relVoList: String? = null
)