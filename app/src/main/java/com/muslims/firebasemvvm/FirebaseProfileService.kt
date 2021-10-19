package com.muslims.firebasemvvm

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.muslims.firebasemvvm.Useree.Companion.toUser
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


object FirebaseProfileService {
    val xpertSlug = "Hello world"
    private const val TAG = "FirebaseProfileService"
    suspend fun getProfileData(userId: String): Useree? {
        val db = FirebaseFirestore.getInstance()
        Log.i(TAG , "get data successfully")
        return try {
            db.collection("users")
                .document(userId).get().await().toUser()

        } catch (e: Exception) {
            Log.e(TAG, "Error getting user details", e)
            FirebaseCrashlytics.getInstance().log("Error getting user details")
            FirebaseCrashlytics.getInstance().setCustomKey("user id", xpertSlug)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    suspend fun addUser(userId: String) {
        val city = hashMapOf(
            "name" to "Los Angeles",
            "id" to "CA",
            "profile_image" to "USA",
            "user_bio" to "asd123"
        )
        val db = FirebaseFirestore.getInstance()
        return
            db.collection("users").document(userId)
                .set(city)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

    }

    suspend fun getPosts(userId: String): Flow<List<Useree>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection("users")
                .document(userId)
                .collection("users")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(message = "Error fetching posts",
                            cause = firebaseFirestoreException)
                        return@addSnapshotListener
                    }
                    val map = querySnapshot!!.documents.mapNotNull { it.toUser() }
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling posts listener")
                listenerRegistration.remove()
            }
        }
    }

    suspend fun getFriends(userId: String): List<Useree> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("users")
                .document(userId)
                .collection("friends").get().await()
                .documents.mapNotNull { it.toUser() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting user friends", e)
            FirebaseCrashlytics.getInstance().log("Error getting user friends")
            FirebaseCrashlytics.getInstance().setCustomKey("user id", xpertSlug)
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }
}