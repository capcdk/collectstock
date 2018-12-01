//package dsls
//
///**
// *
// * Created by Chendk on 2018/9/20
// */
//inline fun <reified T> entityWrapper(conditions: EntityWrapper<T>.() -> Unit): EntityWrapper<T> {
//    val ew = EntityWrapper<T>()
//    ew.conditions()
//    return ew
//}
//
//inline fun <reified T> BaseMapper<T>.selectList(conditions: EntityWrapper<T>.() -> Unit): List<T> {
//    val ew = EntityWrapper<T>()
//    ew.conditions()
//    return this.selectList(ew)
//}
//
//inline fun <reified T> BaseMapper<T>.selectFirst(conditions: EntityWrapper<T>.() -> Unit): T? {
//    val ew = EntityWrapper<T>()
//    ew.conditions()
//    return this.selectList(ew)?.firstOrNull()
//}