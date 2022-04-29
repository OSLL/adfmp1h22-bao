package com.cabpacob.bao

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

        val mode = intent.getStringExtra("Mode")
        val firstName = intent.getStringExtra("First")
        val secondName = intent.getStringExtra("Second")

        model = Model(
            this,
            field,
            firstName!!,
            hand1,
            secondName,
            hand2,
            statusView,
            mode == "GameWithBot"
        )
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
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("New turn")
            builder.setCancelable(true)
            builder.setPositiveButton(
                "Ok"
            ) { dialog, _ ->

                // Кнопка ОК
                dialog.dismiss() // Отпускает диалоговое окно
                nextTurn(false)
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
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
        model.waitTurn()
    }
}