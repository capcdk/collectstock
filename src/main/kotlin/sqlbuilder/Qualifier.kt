package sqlbuilder

/**
 * mysql行修饰符
 * Created by Chendk on 2018/8/1
 */
enum class Qualifier(originSql: String) {
    DEFAULT_VALUE("DEFAULT {0}"),
    NOT_NULL("NOT NULL"),
    UNSIGNED("unsigned"),
    AUTO_INCREMENT("AUTO_INCREMENT")
}