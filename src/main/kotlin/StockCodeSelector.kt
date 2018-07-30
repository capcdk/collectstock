import net.dongliu.requests.Requests
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

/**
 * Created by Chendk on 2018/7/20
 */
class StockCodeSelector {

    // 根地址
    val url = "http://quote.eastmoney.com/stocklist.html"

    val codeRegex = Regex("(?<=\\().*(?=\\))")

    // 初始化爬取
    init {
        this.boot(this.url)
    }

    /**
     * 入口 ,启动爬虫
     */
    private fun boot(url: String) {

        // 拿到首页的HTML
        val html = this.soup(url)

        // 得到所有的标签 ,也就是分类
        val codes =
                (html.select("a[href*=http://quote.eastmoney.com/sh]") + html.select("a[href*=http://quote.eastmoney.com/sz]"))
                        .flatMap { it.childNodes() }
                        .mapNotNull { codeRegex.find(it.toString())?.value }
        println("total : ${codes.size}")
    }


    /**
     * 得到所有的图片并保存
     */
    private fun getImgs(detail: MutableMap.MutableEntry<String, String>) {

        // 选中所有图片
        var imgs = this.soup(detail.value).select("#picture img")

        // 以标题为目录名
        var filePath = "/opt/imgs/" + detail.key

        // 建立文件夹
        File(filePath).mkdir()

        // 声明一个FLAG ,用于图片名
        var flag = 0
        for (img in imgs) {

            // 利用Requests保存图片
            Requests.get(img.attr("src"))
                    .send()
                    .toFileResponse(File(filePath + "/" + flag.toString() + ".jpg").toPath())

            // 保存一张 ,给flag++
            flag++

            // 打印提示到控制台
            println("""${detail.key} 中的第 ${flag} 张妹子图 : ${img.attr("src")}""".trimIndent())
        }
    }


    /**
     * 获得soup对象
     */
    private fun soup(url: String): Document {

        /**
         * 利用指定的Header链接到URL ,并拉取资源
         */
        return Jsoup.connect(url)
                .headers(mapOf("User-Agent" to "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3298.3 Safari/537.36",
                        "Host" to "http://quote.eastmoney.com"
                )).get()
    }
}