package com.olamachia.simplemviproject.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.olamachia.simplemviproject.data.repository.MainRepository
import com.olamachia.simplemviproject.utils.MainIntent
import com.olamachia.simplemviproject.utils.viewstate.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: MainRepository
): ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)

    val state: StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }


    private fun handleIntent(){
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    MainIntent.FetchUser -> fetchUser()
                }
            }
        }
    }

    private fun fetchUser(){
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Users(repository.getUser())
            } catch (e: Exception){
                MainState.Error(e.localizedMessage)
            }
        }
    }


}