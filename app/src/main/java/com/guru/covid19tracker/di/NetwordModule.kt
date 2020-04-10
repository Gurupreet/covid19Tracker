package com.guru.covid19tracker.di

import com.guru.covid19tracker.api.ApiService
import com.guru.covid19tracker.api.MainApiDataSource
import com.guru.covid19tracker.api.MainApiDataSourceImpl
import com.guru.covid19tracker.api.iterceptors.ConnectivityInterceptor
import com.guru.covid19tracker.api.iterceptors.ConnectivityInterceptorImpl
import org.koin.dsl.module

val NetworkModule = module {
    single { ConnectivityInterceptorImpl(get()) as ConnectivityInterceptor }
    single { ApiService(get()) }
    single { MainApiDataSourceImpl(get()) as MainApiDataSource }
}