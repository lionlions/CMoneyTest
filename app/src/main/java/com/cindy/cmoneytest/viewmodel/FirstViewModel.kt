package com.cindy.cmoneytest.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstViewModel : ViewModel() {

    private val TAG: String = javaClass.simpleName

    private val _navigateToListFragment = MutableLiveData<Event<Boolean>>()

    val navigateToListFragment: LiveData<Event<Boolean>>
        get() = _navigateToListFragment

    fun onRequestClick() {
        Log.v(TAG, "=== onRequestClick ===")
        Log.i(TAG, "goto next page")
        _navigateToListFragment.value = Event(true)
    }

}