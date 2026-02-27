package com.example.investmentmanager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.investmentmanager.database.AppDatabase
import com.example.investmentmanager.database.User
import com.example.investmentmanager.database.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    suspend fun findByEmail(email: String): User? {
        return repository.findByEmail(email)
    }
}