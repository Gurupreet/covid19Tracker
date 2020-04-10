package com.guru.covid19tracker.api

import androidx.lifecycle.LiveData
import com.guru.covid19tracker.models.State

interface MainApiDataSource {
    val getApiData: LiveData<List<State>>
    val error: LiveData<String>

    suspend fun fetchData()
    suspend fun refreshData()
}