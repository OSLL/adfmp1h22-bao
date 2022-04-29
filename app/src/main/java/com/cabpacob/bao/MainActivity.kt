package com.cabpacob.bao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hotSeat.setOnClickListener {
            val intent = Intent(this, GetUsernameHotSeatActivity::class.java)
            startActivity(intent)
        }

        gameWithBot.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("Mode", "GameWithBot")
            intent.putExtra("First", "Player")
            startActivity(intent)
        }

        rulesButton.setOnClickListener {
            val intent = Intent(this, RulesActivity::class.java)
            startActivity(intent)
        }

        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        statisticsButton.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }
}