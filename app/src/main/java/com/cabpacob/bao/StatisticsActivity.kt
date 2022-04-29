package com.cabpacob.bao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val filename = "statistics"

        val statistics = try {
            val stream = openFileInput(filename)
            Json.decodeFromStream(stream)
        } catch (e: FileNotFoundException) {
            Statistics()
        }

        val data = mutableListOf("Name", "Win", "Lose")

        data.addAll(statistics.map
            .toList()
            .map { listOf(it.first, it.second.first.toString(), it.second.second.toString()) }
            .flatMap { it.asIterable() })

        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, data)
        statisticsGridView.adapter = adapter

        statisticsGridView.numColumns = 3

    }
}