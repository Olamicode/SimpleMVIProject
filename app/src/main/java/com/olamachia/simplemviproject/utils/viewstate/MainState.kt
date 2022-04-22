package com.olamachia.simplemviproject.utils.viewstate

import com.olamachia.simplemviproject.data.model.User

sealed class MainState{
    object Idle: MainState()
    object Loading: MainState()
    data class Users(val users: List<User>): MainState()
    data class Error(val error: String?): MainState()
}
