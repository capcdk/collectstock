package business.stockdata

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode

/**
 * Created by Chendk on 2018/7/20
 */
class StockSelector {

    // 根地址
    val stockCodeUrl = "http://quote.eastmoney.com/stocklist.html"
    val stockHistoryUrl = "http://www.aigaogao.com/tools/history.html?s="

    val stockCodeHtml: Document

    val stockCodeRowName = "股票代码"
    val codeRegex = Regex("(?<=\\().*(?=\\))")

    // 初始化爬取
    init {
        // 拿到首页的HTML
        stockCodeHtml = this.soup(stockCodeUrl)
    }

    /**
     * 获取股票代码
     */
    fun getStockCodes(): List<String> {

        // 得到所有的标签 ,也就是分类
        return (stockCodeHtml.select("a[href*=http://quote.eastmoney.com/sh]")
                + stockCodeHtml.select("a[href*=http://quote.eastmoney.com/sz]"))
                .flatMap { it.childNodes() }
                .mapNotNull { codeRegex.find(it.toString())?.value }
    }

    fun getStockHeaders(): List<String> {
        val historyDataRows = getStockHistoryDataByCode(600207.toString()) ?: throw RuntimeException("获取不到数据项名称")
        return getHeaderListFromRows(historyDataRows)
    }

    fun getStockHistoryData(code: String): List<Map<String, String>> {
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

    private fun getStockHistoryDataByCode(code: String): List<Node>? =
            this.soup("$stockHistoryUrl$code").select("table[class=data]")?.last()?.childNodes()?.first()?.childNodes()

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


    /**
     * 获得soup对象
     */
    private fun soup(url: String): Document {

        /**
         * 利用指定的Header链接到URL ,并拉取资源
         */
        return Jsoup.connect(url)
                .headers(mapOf("User-Agent" to "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3298.3 Safari/537.36"
                )).get()
    }
}