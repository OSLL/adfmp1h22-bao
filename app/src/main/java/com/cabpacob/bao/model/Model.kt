package com.cabpacob.bao.model

import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.cabpacob.bao.GameActivity

class Model(
    private val activity: GameActivity,
    buttons: List<List<Button>>,
    firstPlayerHand: TextView,
    secondPlayerHand: TextView
) {
    private val field =
        buttons.mapIndexed { x, row ->
            row.mapIndexed { y, button ->
                ButtonHandler(
                    this,
                    button,
                    x,
                    y
                )
            }
        }
    private val firstPlayer: Player
    private val secondPlayer: Player
    private var currentPlayer = CurrentPlayer.FIRST_PLAYER

    init {
        val firstHand = TextHandler(firstPlayerHand)
        firstPlayer = Player(firstHand, field.subList(2, 4).reversed())
        val secondHand = TextHandler(secondPlayerHand)
        secondPlayer = Player(secondHand, field.subList(0, 2))

        field[1][3].value = 6 //second player init
        field[1][2].value = 2
        field[1][1].value = 2
        firstPlayer.hand.value = 22

        field[2][4].value = 6 //first player init
        field[2][5].value = 2
        field[2][6].value = 2
        secondPlayer.hand.value = 22
    }

    fun clearHighlighting() {
        field.forEach { row -> row.forEach { it.highlight(ButtonStatus.EMPTY) } }
    }

    private fun getCurrentPlayer(): Player {
        return if (currentPlayer == CurrentPlayer.FIRST_PLAYER) firstPlayer else secondPlayer
    }

    fun getSelectableButtons(): List<ButtonHandler> {
        return getCurrentPlayer().field[1].filter {
            it.value > 0
        }
    }

    private fun changePlayer() {
        currentPlayer =
            if (currentPlayer == CurrentPlayer.SECOND_PLAYER) CurrentPlayer.FIRST_PLAYER else CurrentPlayer.SECOND_PLAYER
    }

    fun select(row: Int, col: Int) {
        Log.d("HEHEHE", "SELECT $row $col")

        val actualRow = if (currentPlayer == CurrentPlayer.SECOND_PLAYER) row else 3 - row

        getCurrentPlayer().let {
            it.field[actualRow][col].value++
            it.hand.value--
        }

        clearHighlighting()



        activity.readyNextTurn()
    }


}