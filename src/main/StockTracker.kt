package main

import javax.swing.SwingUtilities

class StockTracker {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                val frame = Frame("Stock Tracker")
                frame.loadButtons()
                frame.loadLabels()
                frame.addLine(false)
            }
        }
    }
}