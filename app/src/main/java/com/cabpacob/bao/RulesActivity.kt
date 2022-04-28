package com.cabpacob.bao

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rules.*

class RulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        rulesVideo.setOnClickListener {
            val youTubeIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=mKRmq44L_7g"))
            startActivity(youTubeIntent)
        }
    }
}