package com.example.investmentmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity tells Room this class is a table in our database
@Entity(tableName = "investment_table")
data class Investment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val broker: String,
    val amount: Double,
    val date: Long,

    val isActive: Boolean = true // <-- ADD THIS LINE
)