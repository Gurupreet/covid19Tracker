package com.guru.covid19tracker.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guru.covid19tracker.App
import com.guru.covid19tracker.models.State
import com.guru.covid19tracker.api.MainApiDataSource
import com.guru.covid19tracker.data.db.MainDao
import com.guru.covid19tracker.models.ApiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainRepositoryImpl(val mainDao: MainDao, val mainApiDataSource: MainApiDataSource) : MainRepository {

    private var _error = MutableLiveData<String>()
    private val _isRefreshed = MutableLiveData<String>()

    override val error: LiveData<String>
        get() = _error

    override val isRefreshed: LiveData<String>
        get() = _isRefreshed

    init {
        mainApiDataSource.apply {
            getApiData.observeForever {
                it?.let { apiData ->
                    App.hasFetched = true
                    _isRefreshed.value = System.currentTimeMillis().toString()
                  //  saveToLocalDatabse(apiData)
                }
            }
            error.observeForever {
                _error.value = it
            }
        }
    }
    override suspend fun getApiData(): LiveData<List<State>> {
            return withContext(Dispatchers.IO) {
                fetchfromDataSource()
                return@withContext mainApiDataSource.getApiData
//                if (!App.hasFetched) {
//                    // We can add complex cache strategy using timestamps or other status. For Now I am using app lifecycle.
//                    //Irregardless It will still have room data once loaded.
//
//                }
                //return@withContext mainDao.getApiDataFromDatabase()
            }
    }

    override suspend fun refreshData() =
       withContext(Dispatchers.IO) {
            mainApiDataSource.refreshData()
        }

    override suspend fun localData(): LiveData<List<State>> {
//        return withContext(Dispatchers.IO) {
//         mainDao.getApiDataFromDatabase()
//        }
        return  MutableLiveData()
    }

    private suspend fun fetchfromDataSource()  = mainApiDataSource.fetchData()

    private fun saveToLocalDatabse(apiData: List<ApiData>) {
        if (apiData.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                mainDao.insertApiData(apiData)
            }
        }
    }
}