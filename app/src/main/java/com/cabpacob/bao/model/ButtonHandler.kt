package com.cabpacob.bao.model

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.cabpacob.bao.GameActivity
import com.cabpacob.bao.R

class ButtonHandler(
    private val model: Model,
    private val button: Button,
    private val row: Int,
    private val col: Int
) {
    var value: Int
        get() = button.text.toString().toInt()
        set(value) {
            button.text = value.toString()
        }

    var status = ButtonStatus.EMPTY

    private fun isNumba(): Boolean {
        return (row == 1 && col == 3) || (row == 2 && col == 4)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun highlight(status: ButtonStatus) {
        this.status = status
        when (status) {
            ButtonStatus.EMPTY -> {
                button.background = button.resources.getDrawable(
                    if (isNumba()) R.drawable.square_button
                    else R.drawable.round_button
                )
                button.setOnClickListener {}
            }
            ButtonStatus.CAN_BE_CHOSEN -> {
                button.background.setTint(button.resources.getColor(R.color.teal_200))
                button.setOnClickListener {
                    model.fixHighlighting()
                    highlight(ButtonStatus.CHOSEN)
                }
            }
            ButtonStatus.CHOSEN -> {
                button.background.setTint(button.resources.getColor(R.color.purple_200))
                button.setOnClickListener {
                    model.select(row, col)
                }
            }
        }
    }
}