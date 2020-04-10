package com.guru.covid19tracker.data.firebase.syncs

import com.guru.covid19tracker.models.News
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.guru.covid19tracker.data.firebase.FirebaseObserverType
import com.guru.covid19tracker.data.firebase.FirestoreResponseCompletionHandler

class NewsFirebaseSync() : FirebaseSync() {
    init {
        queryRef = mDatabaseRef!!.child("news").orderByChild("created_at").limitToFirst(200);
    }

    fun startFutureFirebaseSync(completionHandler: FirestoreResponseCompletionHandler) {
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                try {
                    val future: News? = snapshot.getValue(News::class.java)
                    completionHandler.onSuccess(future,
                        FirebaseObserverType.CHILD_ADDED
                    )
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }
            override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
                try {
                    val future: News? = snapshot?.getValue(News::class.java)
                    completionHandler.onSuccess(future,
                        FirebaseObserverType.CHILD_CHANGED
                    )
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                try {
                    val future: News? = snapshot?.getValue(News::class.java)
                    completionHandler.onSuccess(future,
                        FirebaseObserverType.CHILD_REMOVED
                    )
                } catch (e: Exception) {
                    completionHandler.onFailure("")
                }
            }

            override fun onCancelled(snapshot: DatabaseError) {}

            override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
        }

        queryRef?.addChildEventListener(childEventListener)
        mChildEventListener = childEventListener
    }
}