package business.ddlconvert

import com.fasterxml.jackson.annotation.JsonIgnore


data class DBTable(
        /**
         * 表名
         */
        var title: String = "",
        /**
         * 表中文名
         */
        var chnname: String = "",
        /**
         * 表注释
         */
        var remark: String? = null,
        /**
         * 表字段
         */
        var fields: List<DBField> = listOf(),
        /**
         * 表索引
         */
        var indexs: List<DBIndex> = listOf(),
        /**
         * 默认选项
         */
        var headers: List<DBHeader> = defaultHeaders,
        @JsonIgnore
        var schema: String = ""
) {

    companion object {
        val defaultHeaders = listOf(
                DBHeader("chnname", false),
                DBHeader("name", true),
                DBHeader("type", false),
                DBHeader("dataType", true),
                DBHeader("remark", true),
                DBHeader("pk", true),
                DBHeader("notNull", true),
                DBHeader("autoIncrement", true),
                DBHeader("defaultValue", true),
                DBHeader("relationNoShow", true)
        )
    }

}

data class DBHeader(
        var fieldName: String = "",
        var relationNoShow: Boolean = false
)