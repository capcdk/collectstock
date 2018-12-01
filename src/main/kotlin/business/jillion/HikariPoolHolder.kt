package business.jillion

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.util.*


/**
 * Created by Chendk on 2018/7/30
 */
object HikariPoolHolder {

    private val hikariDataSource: HikariDataSource
    private val src = "mysql-config.yml"

    init {
        val mapper = ObjectMapper().apply {
            this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
        val yamlFactory = YAMLFactory()
        val dbNode = mapper.readTree<JsonNode>(yamlFactory.createParser(ClassLoader.getSystemClassLoader().getResource(src)))
        val config = HikariConfig(dbNode.toLinkedProperties())
        config.isAutoCommit = false
        config.addDataSourceProperty("rewriteBatchedStatements", "true")
        hikariDataSource = HikariDataSource(config)
    }

    fun getConnection(): Connection = hikariDataSource.connection

}

fun JsonNode.toLinkedProperties(): Properties {
    val properties = Properties()
    this.fields().forEach {
        val node = it.value
        val key = it.key
        if (node.isValueNode) {
            properties[it.key] = node.asText()
        } else {
            node.toLinkedProperties().forEach { pKey, pValue ->
                properties["$key.$pKey"] = pValue
            }
        }
    }
    return properties
}