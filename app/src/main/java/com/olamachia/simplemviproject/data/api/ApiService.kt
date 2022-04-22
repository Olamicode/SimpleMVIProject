package com.olamachia.simplemviproject.data.api

import com.olamachia.simplemviproject.data.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

}