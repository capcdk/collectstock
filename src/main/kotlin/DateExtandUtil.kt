import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Chendk on 2018/5/7
 */

private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.PRC)

private val simpleYMDFormatter = DateTimeFormatter.ofPattern("yy-MM-dd", Locale.PRC)

private val ymdFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.PRC)

fun LocalDateTime.toPlainString(): String = this.format(dateTimeFormatter)

fun LocalDateTime.toSimpleYMDString(): String = this.format(simpleYMDFormatter)

fun LocalDateTime.toYMDString(): String = this.format(ymdFormatter)

fun Date.toPlainString(): String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)

fun Date.toYMDString(): String = SimpleDateFormat("yyyy-MM-dd").format(this)

fun Date.toSimpleYMDString(): String = SimpleDateFormat("yy-MM-dd").format(this)

fun Date.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())

fun LocalDateTime.withMaxTime(): LocalDateTime = this.with(java.time.LocalTime.MAX)

fun LocalDateTime.withMinTime(): LocalDateTime = this.with(java.time.LocalTime.MIN)

