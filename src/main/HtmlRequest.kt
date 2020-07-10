package main

import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException
import javax.swing.SwingUtilities

class HtmlRequest {
    class MyCallBack(private val stockLine: StockLine): Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            if (!response.isSuccessful) {
                println("Unsuccessful response")
                response.close()
                stockLine.setFailedResponse()
                return
            }

            val doc = Jsoup.parse(response.body?.string())
            response.close()
            val elements = doc.select("span[class=priceText__1853e8a5]")
            if (elements.isEmpty()) return
            val priceElement = elements[0]
            SwingUtilities.invokeLater {
                stockLine.updatePriceData(priceElement.text())
            }
            println("Price received")
        }

    }

    private val client = OkHttpClient()

    fun getStockPage(stockLine: StockLine, market: String, ticker: String) {
        val request = Request.Builder()
                .url("https://www.bloomberg.com/quote/$ticker:$market")
                .build()
        client.newCall(request).enqueue(MyCallBack(stockLine))
        println("Request sent")
    }
}