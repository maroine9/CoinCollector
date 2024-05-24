package com.example.coincollector

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
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
        val entries = mutableListOf<Entry>()
        var totalValue = 0f

        coins.forEachIndexed { index, coin ->
            totalValue += coin.value.toFloat()
            entries.add(Entry(index.toFloat(), totalValue))
        }

        val dataSet = LineDataSet(entries, "Évolution de la Valeur Totale").apply {
            color = resources.getColor(R.color.teal_200)
            valueTextColor = resources.getColor(R.color.white)
            setCircleColor(resources.getColor(R.color.teal_200))
            lineWidth = 2f
            circleRadius = 4f
            setDrawCircleHole(false)
            setDrawValues(true)
        }

        val lineData = LineData(dataSet)
        chart.data = lineData

        chart.apply {
            description = Description().apply { text = "Évolution de la Valeur Totale" }
            setNoDataTextColor(resources.getColor(R.color.white))
            setNoDataText("No data available.")
            setBackgroundColor(resources.getColor(R.color.black))
            invalidate()
        }

        val xAxis: XAxis = chart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            textColor = resources.getColor(R.color.white)
            setDrawGridLines(false)
        }

        val yAxisLeft: YAxis = chart.axisLeft
        yAxisLeft.apply {
            textColor = resources.getColor(R.color.white)
            setDrawGridLines(true)
        }

        val yAxisRight: YAxis = chart.axisRight
        yAxisRight.isEnabled = false
    }
}
