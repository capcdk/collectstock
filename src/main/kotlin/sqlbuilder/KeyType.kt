package sqlbuilder

/**
 * innodb索引类型
 * Created by Chendk on 2018/8/1
 */
enum class KeyType(val type: String) {
    BTREE("BTREE"),
    HASH("HASH")
}