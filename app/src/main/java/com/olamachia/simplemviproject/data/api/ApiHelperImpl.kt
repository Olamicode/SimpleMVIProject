package com.olamachia.simplemviproject.data.api

import com.olamachia.simplemviproject.data.model.User

class ApiHelperImpl(private val apiService: ApiService): ApiHelper {

    override suspend fun getUsers(): List<User> {
        return apiService.getUsers()
    }

}