package com.cabpacob.bao

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_end_game.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class EndGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        assert (intent.hasExtra("winner") && intent.hasExtra("loser"))

        val winner = intent.getStringExtra("winner")!!
        val loser = intent.getStringExtra("loser")!!

        winnerName.text = "$winner win"
        loserName.text = "$loser lose"

        mainMenu.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val filename = "statistics"

        val statistics = try {
            val stream = openFileInput(filename)
            Json.decodeFromStream(stream)
        }
        catch (e: FileNotFoundException) {
            Statistics()
        }

        statistics.registerWin(winner)
        statistics.registerLose(loser)


//        stream.writeObject(statistics)
        val json = Json.encodeToString(Statistics.serializer(), statistics)
        openFileOutput(filename, Context.MODE_PRIVATE).write(json.toByteArray())
    }

}