package com.cabpacob.bao.model

import android.widget.Button
import android.widget.TextView

class Model(buttons: List<List<Button>>, firstPlayerHand: TextView, secondPlayerHand: TextView) {
    private val field =
        buttons.mapIndexed { x, row -> row.mapIndexed { y, button -> ButtonHandler(button, x, y) } }
    private val firstPlayer: Player
    private val secondPlayer: Player

    init {
        val firstHand = TextHandler(firstPlayerHand)
        firstPlayer = Player(firstHand, field.subList(0, 2))
        val secondHand = TextHandler(secondPlayerHand)
        secondPlayer = Player(secondHand, field.subList(2, 4))

        field[1][3].value = 6 //first player init
        field[1][2].value = 2
        field[1][1].value = 2
        firstPlayer.hand.value = 22

        field[2][4].value = 6 //second player init
        field[2][5].value = 2
        field[2][6].value = 2
        secondPlayer.hand.value = 22
    }


}