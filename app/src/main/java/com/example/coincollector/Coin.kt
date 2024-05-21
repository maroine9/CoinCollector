package com.example.coincollector

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Coin(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val year: String,
    val rarity: String,
    val quantity: Int,
    val value: Double,
    val imagePath: String
)
