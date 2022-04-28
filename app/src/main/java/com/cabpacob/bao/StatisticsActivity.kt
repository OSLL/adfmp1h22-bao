package com.cabpacob.bao

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_statistics.*

class StatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val data = mutableListOf(
            "Name", "Wins", "Loses",
            "Vasya Pupkin", "228", "322",
            "Petya Vasin", "322", "228"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, data)
        statisticsGridView.adapter = adapter

        statisticsGridView.numColumns = 3

    }
}