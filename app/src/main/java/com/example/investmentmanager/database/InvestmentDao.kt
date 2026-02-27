package com.example.investmentmanager.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface InvestmentDao {

    // Insert a new investment
    @Insert
    suspend fun insert(investment: Investment)

    // Update an existing one
    @Update
    suspend fun update(investment: Investment)

    // Delete one
    @Delete
    suspend fun delete(investment: Investment)

    // Get ALL investments and watch for changes
    // LiveData automatically updates our app when the data changes
    // This will now get ONLY active investments
    @Query("SELECT * FROM investment_table WHERE isActive = 1 ORDER BY date DESC")
    fun getActiveInvestments(): LiveData<List<Investment>>

    // This new function gets ONLY archived investments
    @Query("SELECT * FROM investment_table WHERE isActive = 0 ORDER BY date DESC")
    fun getArchivedInvestments(): LiveData<List<Investment>>
}