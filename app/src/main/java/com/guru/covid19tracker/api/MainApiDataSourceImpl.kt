package com.guru.covid19tracker.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guru.covid19tracker.models.State
import java.lang.Exception

class MainApiDataSourceImpl(private val apiService: ApiService) : MainApiDataSource {
    private val _apiLiveData = MutableLiveData<List<State>>()
    private val _error = MutableLiveData<String>()

    override val getApiData: LiveData<List<State>>
        get() = _apiLiveData

    override val error: LiveData<String>
        get() = _error

    override suspend fun refreshData() = fetchData()

    override suspend fun fetchData() {
        try {
            var result = apiService.getRepositoryAsync().await()
            if (result.isSuccessful) {
                // We are not using api set anymore as api for covid was very unstable so we moved to firebase for api modification and providing data
            } else {
                _error.postValue("Failed to load data")
            }
        } catch (e: Exception) {
            _error.postValue("Failed to load data")
        }
    }
}