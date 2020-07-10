package main

import java.awt.Rectangle
import javax.swing.JButton
import javax.swing.JTextField
import javax.swing.SwingUtilities

class StockLine(private val frame: Frame, private val removable: Boolean) {
    private val marketJTextField = JTextField()
    private val tickerJTextField = JTextField()
    private val buyPriceJTextField = JTextField()
    private val currencyJTextField = JTextField()
    private val removeButton = JButton("- Remove stock")

    fun addBoxes(offset: Int) {
        marketJTextField.bounds = Rectangle(frame.marketJLabel.x, frame.marketJLabel.y + 35 + offset * 40, frame.marketJLabel.width, 25)
        tickerJTextField.bounds = Rectangle(frame.tickerJLabel.x, marketJTextField.y, frame.tickerJLabel.width, marketJTextField.height)
        buyPriceJTextField.bounds = Rectangle(frame.buyPriceJLabel.x, tickerJTextField.y, frame.buyPriceJLabel.width, tickerJTextField.height)
        currencyJTextField.bounds = Rectangle(frame.currencyJLabel.x, buyPriceJTextField.y, frame.currencyJLabel.width, buyPriceJTextField.height)

        frame.add(marketJTextField)
        frame.add(tickerJTextField)
        frame.add(buyPriceJTextField)
        frame.add(currencyJTextField)
    }

    fun addButton() {
        if (!removable) return
        removeButton.bounds = Rectangle(currencyJTextField.x + currencyJTextField.width + 20, currencyJTextField.y, 200, currencyJTextField.height)
        removeButton.addActionListener {
            SwingUtilities.invokeLater {
                if (frame.isAncestorOf(marketJTextField)) frame.remove(marketJTextField)
                if (frame.isAncestorOf(tickerJTextField)) frame.remove(tickerJTextField)
                if (frame.isAncestorOf(buyPriceJTextField)) frame.remove(buyPriceJTextField)
                if (frame.isAncestorOf(currencyJTextField)) frame.remove(currencyJTextField)
                if (frame.isAncestorOf(removeButton)) frame.remove(removeButton)

                frame.invalidate()
                frame.repaint()

                frame.removeStockLine(this)
            }
        }
        frame.add(removeButton)
    }

    fun shiftUpByOneLine() {
        marketJTextField.bounds = Rectangle(frame.marketJLabel.x, marketJTextField.y - 40, frame.marketJLabel.width, 25)
        tickerJTextField.bounds = Rectangle(frame.tickerJLabel.x, marketJTextField.y, frame.tickerJLabel.width, marketJTextField.height)
        buyPriceJTextField.bounds = Rectangle(frame.buyPriceJLabel.x, tickerJTextField.y, frame.buyPriceJLabel.width, tickerJTextField.height)
        currencyJTextField.bounds = Rectangle(frame.currencyJLabel.x, buyPriceJTextField.y, frame.currencyJLabel.width, buyPriceJTextField.height)

        removeButton.bounds = Rectangle(currencyJTextField.x + currencyJTextField.width + 20, currencyJTextField.y, 200, currencyJTextField.height)
    }
}