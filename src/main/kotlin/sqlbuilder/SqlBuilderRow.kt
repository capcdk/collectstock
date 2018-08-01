package sqlbuilder

import java.text.MessageFormat

/**
 * Created by Chendk on 2018/8/1
 */
enum class SqlBuilderRow(val originSql: String) {

    /**
     * 主键，e.g `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT
     */
    ID_ROW("`{0}` bigint(20) unsigned NOT NULL AUTO_INCREMENT"),
    /**
     * 主键定义，e.g PRIMARY KEY (`id`)
     */
    ID_KEY("PRIMARY KEY (`{0}`)"),
    /**
     * 普通行，e.g `rowName` jdbcType(dataSize) qualifier[]
     * @jdbcType
     * @see java.sql.JDBCType
     */
    COMMON_ROW("`{0}` {1}({2}) {3}"),
    /**
     * 普通索引，e.g KEY keyName(`rowName`) USING keyType
     * @param keyType
     * @see KeyType
     */
    COMMON_KEY("KEY {0}(`{1}`) USING {2}");

}