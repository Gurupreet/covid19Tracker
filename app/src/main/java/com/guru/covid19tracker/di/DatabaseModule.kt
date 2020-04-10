package com.guru.covid19tracker.di

import com.guru.covid19tracker.data.db.MainDatabase
import org.koin.dsl.module

val DatabaseModule = module {
        single { MainDatabase.getInstance(get()) }
        single { get<MainDatabase>().mainDao() }
}