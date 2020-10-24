package main

import java.awt.Rectangle
import java.io.File
import java.lang.StringBuilder
import javax.swing.*

class Frame(title: String): JFrame(title) {
    val htmlRequest = HtmlRequest()
    private var actualWidth = 960
    private var actualHeight = 540
    private val stockList = ArrayList<StockLine>()
    private lateinit var calculateJButton: JButton
    private lateinit var saveJButton: JButton
    private lateinit var addLineJButton: JButton
    lateinit var marketJLabel: JLabel
    lateinit var tickerJLabel: JLabel
    lateinit var buyPriceJLabel: JLabel
    lateinit var currencyJLabel: JLabel
    lateinit var shareNoJLabel: JLabel
    lateinit var currentPriceJLabel: JLabel
    lateinit var gainsPerShareJLabel: JLabel
    lateinit var totalGainsJLabel: JLabel

    init {
        setSize(960, 540)
        extendedState = MAXIMIZED_BOTH
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        layout = null
        actualWidth = bounds.width
        actualHeight = bounds.height - 40
    }

    fun loadButtons() {
        calculateJButton = JButton("Calculate data")
        calculateJButton.bounds = Rectangle(actualWidth / 2 - 210, actualHeight - 60, 200, 50)
        calculateJButton.addActionListener {
            for (stockLine in stockList) {
                stockLine.requestData()
            }
        }
        add(calculateJButton)

        saveJButton = JButton("Save stock data")
        saveJButton.bounds = Rectangle(calculateJButton.x + calculateJButton.width + 20, calculateJButton.y, 200, 50)
        saveJButton.addActionListener {
            val stringBuilder = StringBuilder()
            for (stockLine in stockList) {
                if (stringBuilder.isNotEmpty()) stringBuilder.append("\n")
                stringBuilder.append(stockLine.getSaveString())
            }
            val file = File("save.stock")
            if (!file.exists() && !file.createNewFile()) {
                JOptionPane.showMessageDialog(null, "Failed to save stock data")
                return@addActionListener
            }
            file.writeText(stringBuilder.toString())
        }
        add(saveJButton)

        addLineJButton = JButton("+ Add new stock")
        addLineJButton.bounds = Rectangle(50, 50 + stockList.size * 40, 200, 25)
        addLineJButton.addActionListener {
            SwingUtilities.invokeLater {
                //Must run in correct thread
                addLine(true)
                addLineJButton.bounds = Rectangle(50, addLineJButton.y + 40, 200, 25)
            }
        }
        add(addLineJButton)

        invalidate()
        repaint()
    }

    fun loadLabels() {
        marketJLabel = JLabel("Market", SwingConstants.CENTER)
        marketJLabel.bounds = Rectangle(50, 20, 100, 25)
        add(marketJLabel)

        tickerJLabel = JLabel("Ticker", SwingConstants.CENTER)
        tickerJLabel.bounds = Rectangle(marketJLabel.x + marketJLabel.width + 20, marketJLabel.y, 100, marketJLabel.height)
        add(tickerJLabel)

        buyPriceJLabel = JLabel("Buy price", SwingConstants.CENTER)
        buyPriceJLabel.bounds = Rectangle(tickerJLabel.x + tickerJLabel.width + 20, tickerJLabel.y, 100, tickerJLabel.height)
        add(buyPriceJLabel)

        currencyJLabel = JLabel("Currency", SwingConstants.CENTER)
        currencyJLabel.bounds = Rectangle(buyPriceJLabel.x + buyPriceJLabel.width + 20, buyPriceJLabel.y, 100, buyPriceJLabel.height)
        add(currencyJLabel)

        shareNoJLabel = JLabel("Shares", SwingConstants.CENTER)
        shareNoJLabel.bounds = Rectangle(currencyJLabel.x + currencyJLabel.width + 20, buyPriceJLabel.y, 100, buyPriceJLabel.height)
        add(shareNoJLabel)

        currentPriceJLabel = JLabel("Current price", SwingConstants.CENTER)
        currentPriceJLabel.bounds = Rectangle(shareNoJLabel.x + shareNoJLabel.width + 240, shareNoJLabel.y, 100, shareNoJLabel.height)
        add(currentPriceJLabel)

        gainsPerShareJLabel = JLabel("Gains per share", SwingConstants.CENTER)
        gainsPerShareJLabel.bounds = Rectangle(currentPriceJLabel.x + currentPriceJLabel.width + 20, currentPriceJLabel.y, 100, currentPriceJLabel.height)
        add(gainsPerShareJLabel)

        totalGainsJLabel = JLabel("Total gains", SwingConstants.CENTER)
        totalGainsJLabel.bounds = Rectangle(gainsPerShareJLabel.x + gainsPerShareJLabel.width + 20, gainsPerShareJLabel.y, 100, gainsPerShareJLabel.height)
        add(totalGainsJLabel)

        invalidate()
        repaint()
    }

    fun loadSave() {
        val file = File("save.stock")
        if (!file.exists()) {
            //If no save file, start with one blank line
            addLine(false)
            return
        }
        var firstLineAdded = false
        file.forEachLine {
            addLine(firstLineAdded)
            if (!firstLineAdded) firstLineAdded = true
            val data = it.split(",")
            stockList[stockList.size - 1].loadSaveData(data[0], data[1], data[2], data[3], data[4])
        }
    }

    private fun addLine(removable: Boolean) {
        val newLine = StockLine(this, removable)
        stockList.add(newLine)
        newLine.addBoxes(stockList.size - 1)
        newLine.addButton()
        newLine.addLabels()

        invalidate()
        repaint()
    }

    fun removeStockLine(stockLine: StockLine) {
        val index = stockList.indexOf(stockLine)
        if (index > 0) {
            addLineJButton.bounds = Rectangle(50, addLineJButton.y - 40, 200, 25)
            stockList.removeAt(index)
            shiftLinesDown(index)
        }
    }

    private fun shiftLinesDown(startIndex: Int) {
        for (i in startIndex until stockList.size) {
            stockList[i].shiftUpByOneLine()
        }

        invalidate()
        repaint()
    }
}