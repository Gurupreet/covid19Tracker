package com.guru.covid19tracker.data.firebase

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.guru.covid19tracker.models.Info
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import java.lang.Exception

object FirebaseManager {
    private val databaseUrl = FirebaseDatabase.getInstance().reference

    fun getInfoData(firestoreResponseCompletionHandler: FirestoreResponseCompletionHandler) {
        val query = databaseUrl.child("info").orderByChild("sort")
        query.keepSynced(true)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                firestoreResponseCompletionHandler.onFailure(error.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list = arrayListOf<Info>()
                for (snapshot in dataSnapshot.children) {
                    if (snapshot.exists()) {
                        try {
                            list.add(snapshot.getValue(Info::class.java)!!)
                        } catch (e: Exception) {
                            Log.d("Exception while mapping data to INFO class", e.localizedMessage)
                        }
                    }
                }
                firestoreResponseCompletionHandler.onSuccess(list, FirebaseObserverType.CHILD_ADDED)
            }
        })
    }

    fun initInsanceID() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Manager", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token

            })
    }
}