package com.olamachia.simplemviproject.ui.main

import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.olamachia.simplemviproject.data.api.ApiHelperImpl
import com.olamachia.simplemviproject.data.api.RetrofitBuilder
import com.olamachia.simplemviproject.data.model.User
import com.olamachia.simplemviproject.databinding.ActivityMainBinding
import com.olamachia.simplemviproject.ui.adapter.MainAdapter
import com.olamachia.simplemviproject.utils.viewmodel.MainViewModel
import com.olamachia.simplemviproject.utils.viewmodel.ViewModelFactory
import com.olamachia.simplemviproject.utils.MainIntent
import com.olamachia.simplemviproject.utils.viewstate.MainState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private var mainAdapter = MainAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val factory = ViewModelFactory(ApiHelperImpl(RetrofitBuilder.apiService))
        mainViewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)

        setupUI()
        observeViewModel()
        setupClicks()

        setContentView(binding.root)

    }

    private fun setupClicks() {
        binding.buttonFetchUser.setOnClickListener {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(MainIntent.FetchUser)
            }
        }
    }

    private fun setupUI() {
        binding.recyclerView.apply {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
    }

    private fun observeViewModel(){
        binding.apply {
            lifecycleScope.launch {
                mainViewModel.state.collect {
                    when (it) {
                        is MainState.Error -> {
                            buttonFetchUser.isVisible = true
                            progressBar.isVisible = false
                            Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                        }
                        is MainState.Idle -> {

                        }
                        is MainState.Loading -> {
                            buttonFetchUser.isVisible = false
                            progressBar.isVisible = true
                        }
                        is MainState.Users -> {
                            buttonFetchUser.isVisible = false
                            progressBar.isVisible = false
                            renderList(it.users)
                        }
                    }
                }
            }
        }
    }

    private fun renderList(users: List<User>) {
        binding.recyclerView.visibility = View.VISIBLE
        users.let { users -> users.let { mainAdapter.addData(it) } }
        mainAdapter.notifyDataSetChanged()
    }

}