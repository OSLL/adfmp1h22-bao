package com.cabpacob.bao.model

import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.cabpacob.bao.GameActivity

class Model(
    private val activity: GameActivity,
    buttons: List<List<Button>>,
    firstPlayerHand: TextView,
    secondPlayerHand: TextView,
    val status: TextView,
    val gameWithBot: Boolean = false
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
        firstPlayer = Player("Player1", firstHand, field.subList(2, 4).reversed())
        val secondHand = TextHandler(secondPlayerHand)
        secondPlayer = if (gameWithBot) {
            Bot("Bot", secondHand, field.subList(0, 2))
        } else {
            Player("Player2", secondHand, field.subList(0, 2))
        }

        field[1][3].value = 6 //second player init
        field[1][2].value = 2
        field[1][1].value = 2
        firstPlayer.hand.value = 22

        field[2][4].value = 6 //first player init
        field[2][5].value = 2
        field[2][6].value = 2
        secondPlayer.hand.value = 22

        status.text = "${firstPlayer.name}'s turn"
    }

    fun clearHighlighting() {
        field.forEach { row -> row.forEach { it.highlight(ButtonStatus.EMPTY) } }
    }

    fun fixHighlighting() {
        field.forEach { row ->
            row.forEach {
                when (it.status) {
                    ButtonStatus.EMPTY -> {}
                    ButtonStatus.CAN_BE_CHOSEN -> {}
                    ButtonStatus.CHOSEN -> it.highlight(ButtonStatus.CAN_BE_CHOSEN)
                    else -> throw Exception()
                }
            }
        }
    }

    fun getCurrentPlayer(): Player {
        return if (currentPlayer == CurrentPlayer.FIRST_PLAYER) firstPlayer else secondPlayer
    }

    fun getOtherPlayer(): Player {
        return if (currentPlayer == CurrentPlayer.FIRST_PLAYER) secondPlayer else firstPlayer
    }

    fun getSelectableButtons(): List<ButtonHandler> {
        return getCurrentPlayer().field[1].filter {
            it.value > 0
        }
    }

    private fun changePlayer() {
        currentPlayer = if (currentPlayer == CurrentPlayer.SECOND_PLAYER) {
            CurrentPlayer.FIRST_PLAYER
        } else {
            CurrentPlayer.SECOND_PLAYER
        }
        status.text = "${getCurrentPlayer().name}'s turn"
    }

    private fun getNextClockwisePit(current: Pair<Int, Int>): Pair<Int, Int> {
        val (first, second) = current

        return if (first == 0) {
            when (second) {
                0 -> Pair(1, 0)
                else -> Pair(0, second - 1)
            }
        } else {
            when (second) {
                7 -> Pair(0, 7)
                else -> Pair(1, second + 1)
            }
        }
    }

    private fun getPrevClockwisePit(current: Pair<Int, Int>): Pair<Int, Int> {
        val (first, second) = current

        return if (first == 0) {
            when (second) {
                7 -> Pair(1, 7)
                else -> Pair(0, second + 1)
            }
        } else {
            when (second) {
                0 -> Pair(0, 0)
                else -> Pair(1, second - 1)
            }
        }
    }

    private fun getPit(coordinates: Pair<Int, Int>): ButtonHandler {
        val (x, y) = coordinates
        return getCurrentPlayer().field[x][y]
    }

    private var waitingAction = WaitingAction.ADD_SEED
    private lateinit var lastSelect: Pair<Int, Int>
    private var inHand = 0


    private var isReadyNextTurn = false

    fun nextTurn() {
        waitingAction = WaitingAction.ADD_SEED
        changePlayer()
        isReadyNextTurn = false
    }

    fun waitTurn() {
        if (currentPlayer == CurrentPlayer.SECOND_PLAYER && gameWithBot) {
            if (!isReadyNextTurn) {
                (secondPlayer as Bot).makeMove(this)
            } else {
                activity.nextTurn(false)
            }
        }

    }

    fun select(row: Int, col: Int) {
        Log.d("HEHEHE", "SELECT $row $col")

        clearHighlighting()

        val actualRow = if (currentPlayer == CurrentPlayer.SECOND_PLAYER) row else 3 - row

        when (waitingAction) {
            WaitingAction.ADD_SEED -> {
                assert(actualRow == 1)
                getCurrentPlayer().let {
                    it.field[actualRow][col].value++
                    it.hand.value--
                }
                when {
                    getOtherPlayer().field[actualRow][col].value > 0 -> {
                        inHand = getOtherPlayer().field[actualRow][col].value
                        getOtherPlayer().field[actualRow][col].value = 0
                        waitingAction = WaitingAction.SELECT_KICHWA
                        status.text = "Select kichwa\n You have $inHand seeds"

                        val fieldRow = getCurrentPlayer().field[actualRow]
                        fieldRow[0].highlight(ButtonStatus.CAN_BE_CHOSEN)
                        fieldRow[7].highlight(ButtonStatus.CAN_BE_CHOSEN)
                    }
                    getCurrentPlayer().field[actualRow][col].value > 1 -> {
                        lastSelect = Pair(actualRow, col)
                        val coordinates = Pair(actualRow, col)

                        getPit(getNextClockwisePit(coordinates)).highlight(ButtonStatus.CAN_BE_CHOSEN)

                        getPit(getPrevClockwisePit(coordinates)).highlight(ButtonStatus.CAN_BE_CHOSEN)
                        inHand = getCurrentPlayer().field[actualRow][col].value
                        getCurrentPlayer().field[actualRow][col].value = 0
                        waitingAction = WaitingAction.SELECT_PIT
                        status.text = "Select pit\nYou have $inHand seeds"
                    }
                    else -> {
                        activity.readyNextTurn()
                        isReadyNextTurn = true
                        status.text = "End turn"
                    }
                }
            }
            WaitingAction.SELECT_KICHWA -> {
                val nextFunction: (Pair<Int, Int>) -> Pair<Int, Int> = {
                    when (col) {
                        0 -> {
                            getNextClockwisePit(it)
                        }
                        7 -> {
                            getPrevClockwisePit(it)
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                }

                var current = Pair(actualRow, col)
                while (inHand > 0) {
                    inHand--
                    getPit(current).value++
                    if (inHand > 0) {
                        current = nextFunction(current)
                    }
                }

                when {
                    current.first == 1 && getOtherPlayer().field[current.first][current.second].value > 0 -> {
                        inHand = getOtherPlayer().field[current.first][current.second].value
                        getOtherPlayer().field[current.first][current.second].value = 0
                        waitingAction = WaitingAction.SELECT_KICHWA
                        status.text = "Select kichwa\n You have $inHand seeds"
                        val fieldRow = getCurrentPlayer().field[actualRow]
                        fieldRow[0].highlight(ButtonStatus.CAN_BE_CHOSEN)
                        fieldRow[7].highlight(ButtonStatus.CAN_BE_CHOSEN)
                    }
                    getCurrentPlayer().field[current.first][current.second].value > 1 -> {
                        lastSelect = current
                        val coordinates = current

                        getPit(getNextClockwisePit(coordinates)).highlight(ButtonStatus.CAN_BE_CHOSEN)

                        getPit(getPrevClockwisePit(coordinates)).highlight(ButtonStatus.CAN_BE_CHOSEN)
                        inHand = getCurrentPlayer().field[current.first][current.second].value
                        getCurrentPlayer().field[current.first][current.second].value = 0
                        waitingAction = WaitingAction.SELECT_PIT
                        status.text = "Select pit\nYou have $inHand seeds"
                    }
                    else -> {
                        activity.readyNextTurn()
                        isReadyNextTurn = true
                        status.text = "End turn"
                    }
                }
            }
            WaitingAction.SELECT_PIT -> {
                val temp = Pair(actualRow, col)
                val nextFunction: (Pair<Int, Int>) -> Pair<Int, Int> = {
                    when (temp) {
                        getNextClockwisePit(lastSelect) -> {
                            getNextClockwisePit(it)
                        }
                        getPrevClockwisePit(lastSelect) -> {
                            getPrevClockwisePit(it)
                        }
                        else -> {
                            throw Exception()
                        }
                    }
                }

                var current = Pair(actualRow, col)
                while (inHand > 0) {
                    inHand--
                    getPit(current).value++
                    if (inHand > 0) {
                        current = nextFunction(current)
                    }
                }

                when {
                    current.first == 1 && getOtherPlayer().field[current.first][current.second].value > 1 -> {
                        inHand = getOtherPlayer().field[current.first][current.second].value
                        getOtherPlayer().field[current.first][current.second].value = 0
                        waitingAction = WaitingAction.SELECT_KICHWA
                        status.text = "Select kichwa\nYou have $inHand seeds"
                        val fieldRow = getCurrentPlayer().field[actualRow]
                        fieldRow[0].highlight(ButtonStatus.CAN_BE_CHOSEN)
                        fieldRow[7].highlight(ButtonStatus.CAN_BE_CHOSEN)
                    }
                    getCurrentPlayer().field[current.first][current.second].value > 1 -> {
                        lastSelect = current
                        val coordinates = current

                        getPit(getNextClockwisePit(coordinates)).highlight(ButtonStatus.CAN_BE_CHOSEN)

                        getPit(getPrevClockwisePit(coordinates)).highlight(ButtonStatus.CAN_BE_CHOSEN)

                        inHand = getCurrentPlayer().field[current.first][current.second].value
                        getCurrentPlayer().field[current.first][current.second].value = 0

                        waitingAction = WaitingAction.SELECT_PIT
                        status.text = "Select pit\nYou have $inHand seeds"
                    }
                    else -> {
                        activity.readyNextTurn()
                        isReadyNextTurn = true
                        status.text = "End turn"
                    }
                }
            }
        }

        waitTurn()

    }

    fun getButtonsForBot(): List<ButtonHandler> {
        return field.flatMap { it.asIterable() }.filter { it.status != ButtonStatus.EMPTY }
    }


}