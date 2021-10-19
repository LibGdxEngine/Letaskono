package com.muslims.firebasemvvm

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class Useree(val userId: String, //Document ID is actually the user id
                  val name: String,
                  val bio: String,
                  val imageUrl: String) : Parcelable {

    companion object {
        fun DocumentSnapshot.toUser(): Useree? {
            try {
                val name = getString("name")!!
                val imageUrl = getString("profile_image")!!
                val bio = getString("user_bio")!!
                return Useree(id, name, bio, imageUrl)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting user profile", e)
                FirebaseCrashlytics.getInstance().log("Error converting user profile")
                FirebaseCrashlytics.getInstance().setCustomKey("userId", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "User"
    }
}