package com.guru.covid19tracker.di

import com.guru.covid19tracker.data.repository.MainRepository
import com.guru.covid19tracker.data.repository.MainRepositoryImpl
import org.koin.dsl.module

val RepositoryModule = module {
    factory { MainRepositoryImpl(get(), get()) as MainRepository }
}