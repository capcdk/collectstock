import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.cfg.MapperConfig
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat

/**
 * Created by Chendk on 2018/11/16
 */
object JsonUtils {

    val log = LoggerFactory.getLogger(this.javaClass)

    val objectMapper = ObjectMapper()
            .setPropertyNamingStrategy(object : PropertyNamingStrategy() {
                override fun nameForGetterMethod(config: MapperConfig<*>, method: AnnotatedMethod, defaultName: String): String {
                    val name = if (method.name.startsWith("is")) {
                        method.name
                    } else
                        defaultName
                    return super.nameForGetterMethod(config, method, name)
                }
            })
            .setDateFormat(SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)!!

//    val objectMapper = ObjectMapper().apply {
//        this.setSerializationInclusion(JsonInclude.Include.ALWAYS)
//        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//        this.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
//        this.dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//
//        //解决long值过大，js掉失精度问题
//        val simpleModule = SimpleModule()
//        simpleModule.addSerializer(Long::class.java, ToStringSerializer.instance)
//        simpleModule.addSerializer(java.lang.Long.TYPE, ToStringSerializer.instance)
//        this.registerModule(simpleModule)
//    }

    fun convertBean2Map(bean: Any): MutableMap<String, Any?> {
        return objectMapper.readValue(objectMapper.writeValueAsString(bean))
    }

    fun serialize(bean: Any): String? {
        return try {
            objectMapper.writeValueAsString(bean)
        } catch (e: Exception) {
            log.error("========> 序列化失败！", e)
            null
        }
    }

    inline fun <reified T> deserialize(jsonString: String?): T? {
        return try {
            objectMapper.readValue<T>(jsonString ?: return null)
        } catch (e: Exception) {
            log.error("========> 反序列化失败！", e)
            null
        }
    }

    inline fun <reified T> clone(bean: T): T? {
        return try {
            objectMapper.readValue<T>(objectMapper.writeValueAsBytes(bean))
        } catch (e: Exception) {
            log.error("========> 反序列化失败！", e)
            null
        }
    }

}