package com.cabpacob.bao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        assert (intent.hasExtra("winner") && intent.hasExtra("loser"))

        winnerName.text = "${intent.getStringExtra("winner")} win"
        loserName.text = "${intent.getStringExtra("loser")} lose"

        mainMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}