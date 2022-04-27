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
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("Mode", "HotSeat")
            startActivity(intent)
        }
    }
}