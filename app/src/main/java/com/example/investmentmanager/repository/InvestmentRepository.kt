package com.example.investmentmanager.repository

import androidx.lifecycle.LiveData
import com.example.investmentmanager.database.Investment
import com.example.investmentmanager.database.InvestmentDao

// The repository is the single source of truth for all app data.
// It just takes the DAO as a parameter.
class InvestmentRepository(private val investmentDao: InvestmentDao) {

    // Change this to call the new function
    val allActiveInvestments: LiveData<List<Investment>> = investmentDao.getActiveInvestments()

    // Add this for the history tab
    val allArchivedInvestments: LiveData<List<Investment>> = investmentDao.getArchivedInvestments()

    // This new function will "archive" an item
    suspend fun archive(investment: Investment) {
        // We copy the investment but set isActive to false
        investmentDao.update(investment.copy(isActive = false))
    }

    suspend fun insert(investment: Investment) {
        investmentDao.insert(investment)
    }

    suspend fun update(investment: Investment) {
        investmentDao.update(investment)
    }

    suspend fun delete(investment: Investment) {
        investmentDao.delete(investment)
    }
}