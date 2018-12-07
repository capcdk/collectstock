package business.ddlconvert


data class DBField(
        /**
         * 字段名
         */
        var name: String = "",
        /**
         * 字段类型
         */
        var type: String = "",
        /**
         * 备注
         */
        var remark: String = "",
        /**
         * 字段中文名
         */
        var chnname: String = "",
        /**
         * 默认值
         */
        var defaultValue: String? = null,
        /**
         * 是否自增
         */
        var autoIncrement: Boolean? = null,
        /**
         * 是否主键
         */
        var pk: Boolean? = null,
        /**
         * 是否非空
         */
        var notNull: Boolean? = null
)