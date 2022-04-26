package com.cabpacob.bao.model

import android.widget.Button

class ButtonHandler(private val button: Button) {
    var value: Int
        get() = button.text.toString().toInt()
        set(value) {
            button.text = value.toString()
        }
}