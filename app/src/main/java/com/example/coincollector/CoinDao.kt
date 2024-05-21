package com.example.coincollector

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface CoinDao {
    @Query("SELECT * FROM coin")
    fun getAll(): List<Coin>

    @Insert
    fun insert(coin: Coin)

    @Update
    fun update(coin: Coin)

    @Delete
    fun delete(coin: Coin)
}
