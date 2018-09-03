package business.stockdata

import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * Created by Chendk on 2018/7/20
 */
class StockSelectorImp1 : StockSelector {

    // 根地址
    val stockHistoryUrl = "http://www.aigaogao.com/tools/history.html?s="

    val stockCodeRowName = "股票代码"

    override suspend fun getStockHeaders(): List<String> {
        val historyDataRows = getStockHistoryDataByCode(600207.toString()) ?: throw RuntimeException("获取不到数据项名称")
        return getHeaderListFromRows(historyDataRows)
    }

    override suspend fun getStockHistoryData(code: String): List<Map<String, String>> {
        //数据行
        val historyDataRows = getStockHistoryDataByCode(code) ?: return emptyList()
        //标题列数组
        val headerSet = getHeaderListFromRows(historyDataRows).toSet()

        return historyDataRows.drop(1).map { row ->
            headerSet.mapIndexed { lineIdx: Int, header: String ->
                when {
                    header == stockCodeRowName -> Pair(header, code)
                    row.childNodeSize() <= lineIdx -> Pair(header, "")
                    else -> {
                        var dataNode = row.childNode(lineIdx)
                        while (dataNode != null && dataNode !is TextNode) {
                            dataNode = dataNode.takeIf { it.childNodeSize() > 0 }?.childNode(0)
                        }
                        Pair(header, dataNode?.toString() ?: "")
                    }
                }
            }.toMap()
        }
    }

    private suspend fun getStockHistoryDataByCode(code: String): List<Node>? {
        return SingletonJsoup.randomDelaySoup("$stockHistoryUrl$code", "www.aigaogao.com").select("table[class=data]")?.last()?.childNodes()?.first()?.childNodes()
    }

    private fun getHeaderListFromRows(historyDataRows: List<Node>): List<String> {
        //标题列数组
        val headerLineList = historyDataRows.first().childNodes().mapNotNull { it.childNode(0).toString() }.dropLast(2).toMutableList()
        headerLineList += stockCodeRowName
        return headerLineList
    }

//    /**
//     * 得到所有的图片并保存
//     */
//    private fun getImgs(detail: MutableMap.MutableEntry<String, String>) {
//
//        // 选中所有图片
//        var imgs = this.soup(detail.value).select("#picture img")
//
//        // 以标题为目录名
//        var filePath = "/opt/imgs/" + detail.key
//
//        // 建立文件夹
//        File(filePath).mkdir()
//
//        // 声明一个FLAG ,用于图片名
//        var flag = 0
//        for (img in imgs) {
//
//            // 利用Requests保存图片
//            Requests.get(img.attr("src"))
//                    .send()
//                    .toFileResponse(File(filePath + "/" + flag.toString() + ".jpg").toPath())
//
//            // 保存一张 ,给flag++
//            flag++
//
//            // 打印提示到控制台
//            println("""${detail.key} 中的第 ${flag} 张妹子图 : ${img.attr("src")}""".trimIndent())
//        }
//    }

}