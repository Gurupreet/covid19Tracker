package com.guru.covid19tracker.data.repository

import androidx.lifecycle.LiveData
import com.guru.covid19tracker.models.State

interface MainRepository {
    suspend fun getApiData(): LiveData<List<State>>
    suspend fun refreshData()
    suspend fun localData(): LiveData<List<State>>
    val isRefreshed: LiveData<String>
    val error: LiveData<String>
}