package com.guru.covid19tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.guru.covid19tracker.api.ApiService
import com.guru.covid19tracker.api.MainApiDataSourceImpl
import com.guru.covid19tracker.data.db.MainDao
import com.guru.covid19tracker.data.db.MainDatabase
import com.guru.covid19tracker.data.repository.MainRepository
import com.guru.covid19tracker.data.repository.MainRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class MainRepositoryTest {
    private lateinit var repository: MainRepository
    private val dao = mock(MainDao::class.java)
    private val service = mock(ApiService::class.java)
    private val remoteDataSource = MainApiDataSourceImpl(service)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock(MainDatabase::class.java)
        `when`(db.mainDao()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = MainRepositoryImpl(dao, remoteDataSource)
    }

    @Test
    fun checkInteractionWithDao() {
        runBlocking {
            verify(dao, never()).getApiDataFromDatabase()
            verifyZeroInteractions(dao)
        }
    }

    @Test
    fun checkIfDatabaseInvoked() {
        runBlocking {
            repository.localData()
            verify(dao, times(1)).getApiDataFromDatabase()
        }
    }

}