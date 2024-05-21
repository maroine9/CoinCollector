package com.example.coincollector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import androidx.room.Room
class ValueChartActivity : AppCompatActivity() {

    private lateinit var chart: LineChart
    private lateinit var db: CoinDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_value_chart)

        chart = findViewById(R.id.chart)
        db = Room.databaseBuilder(
            applicationContext,
            CoinDatabase::class.java, "coins.db"
        ).allowMainThreadQueries().build()

        loadChartData()
    }

    private fun loadChartData() {
        val coins = db.coinDao().getAll()
        val entries = coins.mapIndexed { index, coin ->
            Entry(index.toFloat(), coin.value.toFloat())
        }

        val dataSet = LineDataSet(entries, "Valeur des Pièces")
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.description = Description().apply { text = "Évolution de la Valeur" }
        chart.invalidate()
    }
}
