package com.muslims.firebasemvvm.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.muslims.firebasemvvm.models.User
import kotlinx.coroutines.tasks.await

object UsersServices {
    private const val TAG = "FirebaseUsersService"
    private const val COLLECTION_NAME = "Users"

    suspend fun getAllUsers(): ArrayList<User> {
        val db = FirebaseFirestore.getInstance()
        val usersList = ArrayList<User>()
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    usersList.add(document.toObject<User>())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }.await()
        return usersList
    }

    suspend fun getUserById(userId :String):User?{
        val db = FirebaseFirestore.getInstance()
        var user: User? = null
        val docRef = db.collection(COLLECTION_NAME).document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    user = document.toObject<User>()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }.await()
        return user
    }

    suspend fun addUser(user:User){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.id)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteUser(userId:String){

    }

    suspend fun updateUser(user:User){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.id)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

}