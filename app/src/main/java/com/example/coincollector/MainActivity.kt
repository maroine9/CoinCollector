package com.example.coincollector

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    private lateinit var coinRecyclerView: RecyclerView
    private lateinit var addCoinFab: FloatingActionButton
    private lateinit var viewChartButton: FloatingActionButton
    private lateinit var coinAdapter: CoinAdapter
    private lateinit var db: CoinDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coinRecyclerView = findViewById(R.id.coinRecyclerView)
        addCoinFab = findViewById(R.id.addCoinFab)
        viewChartButton = findViewById(R.id.viewChartButton)

        db = Room.databaseBuilder(
            applicationContext,
            CoinDatabase::class.java, "coins.db"
        ).allowMainThreadQueries().build()

        coinRecyclerView.layoutManager = LinearLayoutManager(this)
        coinAdapter = CoinAdapter(emptyList())
        coinRecyclerView.adapter = coinAdapter

        addCoinFab.setOnClickListener {
            val intent = Intent(this, AddCoinActivity::class.java)
            startActivity(intent)
        }

        viewChartButton.setOnClickListener {
            val intent = Intent(this, ValueChartActivity::class.java)
            startActivity(intent)
        }

        loadCoins()
    }

    override fun onResume() {
        super.onResume()
        loadCoins()
    }

    private fun loadCoins() {
        val coins = db.coinDao().getAll()
        Log.d("MainActivity", "Coins loaded: $coins")
        coinAdapter.updateCoins(coins)
    }
}