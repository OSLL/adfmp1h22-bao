package com.cabpacob.bao.model

import android.widget.TextView

class TextHandler(private val textView: TextView) {
    var value: Int
        get() = textView.text.toString().toInt()
        set(value) {
            textView.text = value.toString()
        }
}
