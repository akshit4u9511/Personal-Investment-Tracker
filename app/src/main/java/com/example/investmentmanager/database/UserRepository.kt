package com.example.investmentmanager.database

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    suspend fun findByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }
}