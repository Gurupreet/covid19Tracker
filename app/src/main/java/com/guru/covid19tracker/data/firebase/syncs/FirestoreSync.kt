package com.guru.covid19tracker.data.firebase.syncs

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

open class FirestoreSync {
    protected var mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    protected var mDocumentRef: DocumentReference? = null
    protected var mQueryRef: Query? = null
    protected var documentSnapshotEventListener: ListenerRegistration? = null
    protected var querySnapshotEventListener: ListenerRegistration? = null

    fun cleanupListener() {
        if (documentSnapshotEventListener != null) {
            documentSnapshotEventListener!!.remove()
            documentSnapshotEventListener = null
        }

        if (querySnapshotEventListener != null) {
            querySnapshotEventListener!!.remove()
            querySnapshotEventListener = null
        }
    }

    fun stopFirestoreSync() {
        if (documentSnapshotEventListener != null) {
            documentSnapshotEventListener!!.remove()
        }
        if (querySnapshotEventListener != null) {
            querySnapshotEventListener!!.remove()
        }
    }
}