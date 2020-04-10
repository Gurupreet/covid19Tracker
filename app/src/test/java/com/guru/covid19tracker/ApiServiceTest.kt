package com.guru.covid19tracker

import com.guru.covid19tracker.api.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class ApiServiceTest {

    private lateinit var service: ApiService

    private lateinit var mockWebServer: MockWebServer


    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun requestRepositoryTest() {
        runBlocking {
            val mockResponse = MockResponse().apply {
                setBody("[ { \"author\": \"gwen001\", \"name\": \"pentest-tools\", \"avatar\": \"https://github.com/gwen001.png\", \"url\": \"https://github.com/gwen001/pentest-tools\", \"description\": \"Custom pentesting tools\", \"language\": \"Python\", \"languageColor\": \"#3572A5\", \"stars\": 1049, \"forks\": 304, \"currentPeriodStars\": 107, \"builtBy\": [ { \"username\": \"gwen001\", \"href\": \"https://github.com/gwen001\", \"avatar\": \"https://avatars0.githubusercontent.com/u/5347721\" } ] }]")
            }
            mockWebServer.enqueue(mockResponse)
            val resultResponse = service.getRepositoryAsync().await()
            val request = mockWebServer.takeRequest()
            assertNotNull(resultResponse)
            assertThat(request.path, `is`("/repositories"))
        }
    }

    @Test
    fun getResponseSizeTest() {
        runBlocking {
            val mockResponse = MockResponse().apply {
                setBody("[ { \"author\": \"gwen001\", \"name\": \"pentest-tools\", \"avatar\": \"https://github.com/gwen001.png\", \"url\": \"https://github.com/gwen001/pentest-tools\", \"description\": \"Custom pentesting tools\", \"language\": \"Python\", \"languageColor\": \"#3572A5\", \"stars\": 1049, \"forks\": 304, \"currentPeriodStars\": 107, \"builtBy\": [ { \"username\": \"gwen001\", \"href\": \"https://github.com/gwen001\", \"avatar\": \"https://avatars0.githubusercontent.com/u/5347721\" } ] }]")
            }
            mockWebServer.enqueue(mockResponse)
            val resultResponse = service.getRepositoryAsync().await()
            val apiDataList = resultResponse.body()
            assertThat(apiDataList?.size, `is`(1))
        }
    }

}