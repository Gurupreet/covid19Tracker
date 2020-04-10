package com.guru.covid19tracker.data.firebase.syncs

import android.util.Log
import com.guru.covid19tracker.models.State
import com.guru.covid19tracker.utils.Filter
import com.google.firebase.firestore.*
import com.guru.covid19tracker.data.firebase.FirebaseObserverType
import com.guru.covid19tracker.data.firebase.FirestoreResponseCompletionHandler
import java.lang.Exception

class StatesFirestoreSync(filter: Filter) : FirestoreSync() {

    init {
        mQueryRef = when (filter) {
            Filter.CONFIRMED -> mFirestore.collection("covid19").orderBy("confirmed", Query.Direction.DESCENDING)
            Filter.ACTIVE -> mFirestore.collection("covid19").orderBy("active", Query.Direction.DESCENDING)
            Filter.DEATHS -> mFirestore.collection("covid19").orderBy("deaths", Query.Direction.DESCENDING)
            Filter.RECOVERED -> mFirestore.collection("covid19").orderBy("recovered", Query.Direction.DESCENDING)
            else -> mFirestore.collection("covid19").orderBy("confirmed", Query.Direction.DESCENDING)
        }
    }

    fun startStateFirebaseSync(completionHandler: FirestoreResponseCompletionHandler) {
        querySnapshotEventListener = mQueryRef?.addSnapshotListener(object :
            EventListener<QuerySnapshot> {
            override fun onEvent(queryDocumentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException?) {
                if (e != null) {
                    Log.e("query error"+ e?.localizedMessage, "")
                    completionHandler.onFailure("")
                    return
                }
                queryDocumentSnapshots?.let {
                    for (dc in queryDocumentSnapshots.documentChanges) {
                        try {
                            when (dc.type) {
                                DocumentChange.Type.ADDED -> completionHandler.onSuccess(dc.document.toObject<State>(
                                    State::class.java!!),
                                    FirebaseObserverType.CHILD_ADDED
                                )
                                DocumentChange.Type.MODIFIED -> completionHandler.onSuccess(dc.document.toObject<State>(
                                    State::class.java!!),
                                    FirebaseObserverType.CHILD_CHANGED
                                )
                                DocumentChange.Type.REMOVED -> completionHandler.onSuccess(dc.document.toObject<State>(
                                    State::class.java!!),
                                    FirebaseObserverType.CHILD_REMOVED
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("query error"+ e?.localizedMessage, "")
                        }
                    }
                    if (queryDocumentSnapshots.size() == 0) {
                        completionHandler.onFailure("No docs found")
                        return
                    }
                }
            }
        })
    }
}