package main

import javax.swing.JOptionPane
import javax.swing.SwingUtilities

class StockTracker {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                val frame = Frame("Stock Tracker")
                frame.loadLabels()
                frame.loadSave()
                frame.loadButtons()
                JOptionPane.showMessageDialog(null, "Welcome to stock tracker!")
            }
        }
    }
}