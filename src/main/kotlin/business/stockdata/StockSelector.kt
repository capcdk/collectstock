package business.stockdata

/**
 * Created by Chendk on 2018/8/3
 */
interface StockSelector {

    /**
     * 获取股票代码
     */
    fun getStockCodes(): List<String> {
        val codeRegex = Regex("(?<=\\().*(?=\\))")
        // 拿到首页的HTML
        val stockCodeHtml = SingletonJsoup.soup("http://quote.eastmoney.com/stocklist.html", "www.aigaogao.com")
        // 得到所有的标签 ,也就是分类
        return (stockCodeHtml.select("a[href*=http://quote.eastmoney.com/sh]")
                + stockCodeHtml.select("a[href*=http://quote.eastmoney.com/sz]"))
                .flatMap { it.childNodes() }
                .mapNotNull { codeRegex.find(it.toString())?.value }
    }

    suspend fun getStockHeaders(): List<String>

    /**
     * 每个map内keySet数组等同于getStockHeaders()
     */
    suspend fun getStockHistoryData(code: String): List<Map<String, String>>
}