package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var buttons: Array<Button>
    private lateinit var resetButton: Button

    // 0 = Player X, 1 = Player O
    private var activePlayer = 0

    // -1 = Empty, 0 = X, 1 = O
    private var gameState = IntArray(9) { -1 }

    // winning combinations
    private val winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )

    private var gameActive = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.main_activity_statusText_textview)

        resetButton = findViewById(R.id.main_activity_reset_button)
        resetButton.visibility = View.INVISIBLE

        buttons = Array(9) { i ->
            val buttonID = "main_activity_cell_btn$i" + "_button"
            val resID = resources.getIdentifier(buttonID, "id", packageName)
            val btn = findViewById<Button>(resID)
            btn.setOnClickListener { onCellClick(it as Button) }
            btn
        }

        resetButton.setOnClickListener {
            resetGame()
        }
    }

    fun onCellClick(cell: Button) {
        val tappedTag = cell.tag.toString().toInt()

        // Ignore click if game is over or button is already pressed
        if (!gameActive || gameState[tappedTag] != -1) {
            return
        }

        gameState[tappedTag] = activePlayer

        if (activePlayer == 0) {
            cell.text = "X"
            cell.setTextColor(Color.BLUE)
            activePlayer = 1
            statusText.text = getString(R.string.player_o_s_turn)
        } else {
            cell.text = "O"
            cell.setTextColor(Color.RED)
            activePlayer = 0
            statusText.text = getString(R.string.player_x_s_turn)
        }

        checkForWin()
    }

    private fun checkForWin() {
        // Check all winning combinations
        for (winningPos in winningPositions) {
            if (gameState[winningPos[0]] == gameState[winningPos[1]] &&
                gameState[winningPos[1]] == gameState[winningPos[2]] &&
                gameState[winningPos[0]] != -1
            ) {
                gameActive = false
                val winner = if (activePlayer == 1) "X" else "O"
                statusText.text = getString(R.string.player_wins, winner)
                resetButton.visibility = View.VISIBLE
                return
            }
        }

        // No -1 values, it's a draw
        if (!gameState.contains(-1)) {
            statusText.text = getString(R.string.it_s_a_draw)
            gameActive = false
            resetButton.visibility = View.VISIBLE
        }
    }

    private fun resetGame() {
        activePlayer = 0
        gameActive = true
        gameState = IntArray(9) { -1 }
        statusText.text = getString(R.string.player_x_s_turn)

        // Clear all buttons
        for (btn in buttons) {
            btn.text = ""
        }

        resetButton.visibility = View.INVISIBLE
    }
}