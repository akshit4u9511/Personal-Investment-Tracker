package com.example.investmentmanager.ui.investments

import android.app.Application
import androidx.lifecycle.*
import com.example.investmentmanager.database.AppDatabase
import com.example.investmentmanager.database.Investment
import com.example.investmentmanager.repository.InvestmentRepository
import kotlinx.coroutines.launch

class InvestmentsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: InvestmentRepository

    // List for "Investments" tab
    val allActiveInvestments: LiveData<List<Investment>>

    // List for "History" tab
    val allArchivedInvestments: LiveData<List<Investment>>

    init {
        // Get references to the DAO and Repository
        val investmentDao = AppDatabase.getDatabase(application).investmentDao()
        repository = InvestmentRepository(investmentDao)
        allActiveInvestments = repository.allActiveInvestments
        allArchivedInvestments = repository.allArchivedInvestments
    }

    // --- ADD THESE FUNCTIONS BACK ---

    // "viewModelScope.launch" runs this in a background thread
    fun insert(investment: Investment) = viewModelScope.launch {
        repository.insert(investment)
    }

    fun update(investment: Investment) = viewModelScope.launch {
        repository.update(investment)
    }

    // --- END OF ADDED FUNCTIONS ---

    fun archive(investment: Investment) = viewModelScope.launch {
        repository.archive(investment)
    }

    // You can keep or remove this one, we aren't using a real "delete" anymore
    fun delete(investment: Investment) = viewModelScope.launch {
        repository.delete(investment)
    }
}