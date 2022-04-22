package com.olamachia.simplemviproject.data.api

import com.olamachia.simplemviproject.data.model.User

interface ApiHelper {

    suspend fun getUsers(): List<User>
}