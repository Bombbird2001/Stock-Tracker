package main

import java.awt.Rectangle
import javax.swing.*

class Frame(title: String): JFrame(title) {
    private var actualWidth = 960
    private var actualHeight = 540
    private val stockList = ArrayList<StockLine>()
    private lateinit var calculateJButton: JButton
    private lateinit var addLineJButton: JButton
    lateinit var marketJLabel: JLabel
    lateinit var tickerJLabel: JLabel
    lateinit var buyPriceJLabel: JLabel
    lateinit var currencyJLabel: JLabel

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
        calculateJButton.bounds = Rectangle(actualWidth / 2 - 100, actualHeight - 60, 200, 50)
        calculateJButton.addActionListener {
            //TODO
        }
        add(calculateJButton)

        addLineJButton = JButton("+ Add new stock")
        addLineJButton.bounds = Rectangle(50, 90, 200, 25)
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

        invalidate()
        repaint()
    }

    fun addLine(removable: Boolean) {
        val newLine = StockLine(this, removable)
        stockList.add(newLine)
        newLine.addBoxes(stockList.size - 1)
        newLine.addButton()

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