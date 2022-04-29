package com.cabpacob.bao

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_get_username_hot_seat.*

class GetUsernameHotSeatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_username_hot_seat)

        applyUsernames.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("Mode", "HotSeat")
            intent.putExtra("First", player1Name.text.toString())
            intent.putExtra("Second", player2Name.text.toString())
            startActivity(intent)
        }
    }
}