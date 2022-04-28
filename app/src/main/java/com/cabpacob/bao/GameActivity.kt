package com.cabpacob.bao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.cabpacob.bao.model.ButtonStatus
import com.cabpacob.bao.model.Model
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {
    private lateinit var model: Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val row1 = listOf(
            button11,
            button12,
            button13,
            button14,
            button15,
            button16,
            button17,
            button18
        )

        val row2 = listOf(
            button21,
            button22,
            button23,
            button24,
            button25,
            button26,
            button27,
            button28
        )

        val row3 = listOf(
            button31,
            button32,
            button33,
            button34,
            button35,
            button36,
            button37,
            button38
        )

        val row4 = listOf(
            button41,
            button42,
            button43,
            button44,
            button45,
            button46,
            button47,
            button48
        )

        val field = listOf(
            row1,
            row2,
            row3,
            row4
        )

        exit.setOnClickListener {
            val intent = Intent(this, ExitActivity::class.java)
            startActivityForResult(intent, 0)
        }
        model = Model(this, field, hand1, hand2, statusView)
        nextTurn(first = true)
    }

    fun endGame() {
        val win = model.getOtherPlayer().name
        val lose = model.getCurrentPlayer().name
        val intent = Intent(this, EndGameActivity::class.java)
        intent.putExtra("winner", win)
        intent.putExtra("loser", lose)
        startActivity(intent)
    }

    fun readyNextTurn() {
        endTurn.setOnClickListener {
            nextTurn(false)
        }
    }

    fun nextTurn(first: Boolean) {
        endTurn.setOnClickListener {}
        if (!first) {
            model.nextTurn()
        }
        val buttons = model.getSelectableButtons()
        if (buttons.isEmpty()) {
            endGame()
        }
        buttons.forEach {
            it.highlight(ButtonStatus.CAN_BE_CHOSEN)
        }
    }
}