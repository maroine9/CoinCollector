package com.example.coincollector

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Coin::class], version = 1)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}
