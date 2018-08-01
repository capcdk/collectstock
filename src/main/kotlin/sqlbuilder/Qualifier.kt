package sqlbuilder

import java.text.MessageFormat

/**
 * mysql行修饰符
 * Created by Chendk on 2018/8/1
 */
enum class Qualifier(val originSql: String) {
    DEFAULT_VALUE("DEFAULT {0}"),
    NOT_NULL("NOT NULL"),
    UNSIGNED("unsigned"),
    AUTO_INCREMENT("AUTO_INCREMENT"),
    COMMENT("COMMENT '{0}'");

    fun buildComment(comment: String) = MessageFormat.format(this.originSql, comment)

    fun buildDefaultValue(defaultVale: String) = MessageFormat.format(this.originSql, defaultVale)
}