package com.guru.covid19tracker

import android.app.Application
import com.guru.covid19tracker.di.DatabaseModule
import com.guru.covid19tracker.di.NetworkModule
import com.guru.covid19tracker.di.RepositoryModule
import com.guru.covid19tracker.di.ViewModelModule
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {
    companion object {
        var hasFetched = false
    }
    override fun onCreate() {
        super.onCreate()
        initFirebase()

        // dependency injection using Koin.
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(listOf(DatabaseModule, NetworkModule, RepositoryModule, ViewModelModule))
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings
        if (FirebaseAuth.getInstance().currentUser == null) {
            FirebaseAuth.getInstance().signInAnonymously()
        }
    }
}