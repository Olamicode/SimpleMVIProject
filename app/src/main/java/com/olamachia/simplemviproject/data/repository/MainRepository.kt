package com.olamachia.simplemviproject.data.repository

import com.olamachia.simplemviproject.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getUser() = apiHelper.getUsers()

}