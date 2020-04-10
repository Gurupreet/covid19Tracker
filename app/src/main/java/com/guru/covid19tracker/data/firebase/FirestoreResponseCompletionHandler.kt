package com.guru.covid19tracker.data.firebase

interface FirestoreResponseCompletionHandler {
    fun onSuccess(result: Any?, observerType: FirebaseObserverType)
    fun onFailure(result: Any?)
}