package main

import java.awt.Rectangle
import java.lang.NumberFormatException
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.swing.*

class StockLine(private val frame: Frame, private val removable: Boolean) {
    private val marketJTextField = JTextField()
    private val tickerJTextField = JTextField()
    private val buyPriceJTextField = JTextField()
    private val currencyJTextField = JTextField()
    private val shareNoJTextField = JTextField()
    private val removeButton = JButton("- Remove stock")
    private val priceJLabel = JLabel("-", SwingConstants.CENTER)
    private val gainsJLabel = JLabel("-", SwingConstants.CENTER)
    private val totalJLabel = JLabel("-", SwingConstants.CENTER)
    private var buyPrice = 0f
    private var shareNo = 0
    private var currentPrice = 0f

    /** Adds all the user input boxes */
    fun addBoxes(offset: Int) {
        marketJTextField.bounds = Rectangle(frame.marketJLabel.x, frame.marketJLabel.y + 35 + offset * 40, frame.marketJLabel.width, 25)
        tickerJTextField.bounds = Rectangle(frame.tickerJLabel.x, marketJTextField.y, frame.tickerJLabel.width, marketJTextField.height)
        buyPriceJTextField.bounds = Rectangle(frame.buyPriceJLabel.x, tickerJTextField.y, frame.buyPriceJLabel.width, tickerJTextField.height)
        currencyJTextField.bounds = Rectangle(frame.currencyJLabel.x, buyPriceJTextField.y, frame.currencyJLabel.width, buyPriceJTextField.height)
        shareNoJTextField.bounds = Rectangle(frame.shareNoJLabel.x, currencyJTextField.y, frame.shareNoJLabel.width, currencyJTextField.height)

        frame.add(marketJTextField)
        frame.add(tickerJTextField)
        frame.add(buyPriceJTextField)
        frame.add(currencyJTextField)
        frame.add(shareNoJTextField)
    }

    /** Adds the remove stock button for this line */
    fun addButton() {
        if (!removable) return
        removeButton.bounds = Rectangle(shareNoJTextField.x + shareNoJTextField.width + 20, shareNoJTextField.y, 200, shareNoJTextField.height)
        removeButton.addActionListener {
            SwingUtilities.invokeLater {
                frame.remove(marketJTextField)
                frame.remove(tickerJTextField)
                frame.remove(buyPriceJTextField)
                frame.remove(currencyJTextField)
                frame.remove(shareNoJTextField)
                frame.remove(removeButton)
                frame.remove(priceJLabel)
                frame.remove(gainsJLabel)
                frame.remove(totalJLabel)

                frame.invalidate()
                frame.repaint()

                frame.removeStockLine(this)
            }
        }
        frame.add(removeButton)
    }

    /** Adds the labels for calculated/retrieved data for this line */
    fun addLabels() {
        priceJLabel.bounds = Rectangle(frame.currentPriceJLabel.x, shareNoJTextField.y, frame.currentPriceJLabel.width, shareNoJTextField.height)
        gainsJLabel.bounds = Rectangle(frame.gainsPerShareJLabel.x, priceJLabel.y, frame.gainsPerShareJLabel.width, priceJLabel.height)
        totalJLabel.bounds = Rectangle(frame.totalGainsJLabel.x, gainsJLabel.y, frame.totalGainsJLabel.width, gainsJLabel.height)

        frame.add(priceJLabel)
        frame.add(gainsJLabel)
        frame.add(totalJLabel)
    }

    /** Shifts everything in this line up by one line */
    fun shiftUpByOneLine() {
        marketJTextField.bounds = Rectangle(frame.marketJLabel.x, marketJTextField.y - 40, frame.marketJLabel.width, 25)
        tickerJTextField.bounds = Rectangle(frame.tickerJLabel.x, marketJTextField.y, frame.tickerJLabel.width, marketJTextField.height)
        buyPriceJTextField.bounds = Rectangle(frame.buyPriceJLabel.x, tickerJTextField.y, frame.buyPriceJLabel.width, tickerJTextField.height)
        currencyJTextField.bounds = Rectangle(frame.currencyJLabel.x, buyPriceJTextField.y, frame.currencyJLabel.width, buyPriceJTextField.height)
        shareNoJTextField.bounds = Rectangle(frame.shareNoJLabel.x, currencyJTextField.y, frame.shareNoJLabel.width, currencyJTextField.height)

        removeButton.bounds = Rectangle(shareNoJTextField.x + shareNoJTextField.width + 20, shareNoJTextField.y, 200, shareNoJTextField.height)

        priceJLabel.bounds = Rectangle(frame.currentPriceJLabel.x, shareNoJTextField.y, frame.currentPriceJLabel.width, shareNoJTextField.height)
        gainsJLabel.bounds = Rectangle(frame.gainsPerShareJLabel.x, priceJLabel.y, frame.gainsPerShareJLabel.width, priceJLabel.height)
        totalJLabel.bounds = Rectangle(frame.totalGainsJLabel.x, gainsJLabel.y, frame.totalGainsJLabel.width, gainsJLabel.height)
    }

    /** Requests the stock data from online page */
    fun requestData() {
        frame.htmlRequest.getStockPage(this, marketJTextField.text, tickerJTextField.text)
    }

    /** Sets labels to indicate if request fails */
    fun setFailedResponse() {
        priceJLabel.text = "Failed to get price"
        gainsJLabel.text = "-"
        totalJLabel.text = "-"
    }

    /** Updates the retrieved price data for this stock */
    fun updatePriceData(text: String) {
        var labelText: String
        try {
            currentPrice = text.toFloat()
            labelText = text + " " + currencyJTextField.text
            calculateData()
        } catch (e: NumberFormatException) {
            labelText = "Failed to get price"
        }
        priceJLabel.text = labelText
    }

    /** Calculates the required data using the retrieved price, if available */
    private fun calculateData() {
        try {
            val df = DecimalFormat("#.####")
            df.roundingMode = RoundingMode.HALF_EVEN
            buyPrice = buyPriceJTextField.text.toFloat()
            val gain = df.format(currentPrice - buyPrice).toFloat()
            gainsJLabel.text = gain.toString() + " " + currencyJTextField.text
            try {
                shareNo = shareNoJTextField.text.toInt()
                totalJLabel.text = df.format(gain * shareNo) + " " + currencyJTextField.text
            } catch (e: NumberFormatException) {
                totalJLabel.text = "Invalid shares"
            }
        } catch (e: NumberFormatException) {
            gainsJLabel.text = "Invalid buy price"
        }
    }
}