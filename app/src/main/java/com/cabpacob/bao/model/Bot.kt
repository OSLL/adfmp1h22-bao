package com.cabpacob.bao.model

class Bot(name: String, hand: TextHandler, field: List<List<ButtonHandler>>) :
    Player(name, hand, field) {
        fun makeMove(model: Model) {
            val buttons = model.getButtonsForBot()

//            val (x, y) = buttons[0].coordinates
            buttons[0].let {
                it.button.performClick()
                it.button.performClick()
            }
        }
}