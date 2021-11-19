package com.cindy.cmoneytest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cindy.cmoneytest.api.ApiRepository

class ViewModelFactory(
    private val repository: ApiRepository? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        modelClass.run {
            when {
                isAssignableFrom(FirstViewModel::class.java) -> {
                    return FirstViewModel() as T
                }
                isAssignableFrom(ListViewModel::class.java) -> {
                    return ListViewModel(repository) as T
                }
                isAssignableFrom(DetailViewModel::class.java) -> {
                    return DetailViewModel() as T
                }
                else -> {
                    throw IllegalAccessException("Unknown ViewModel Class")
                }
            }
        }
    }

}