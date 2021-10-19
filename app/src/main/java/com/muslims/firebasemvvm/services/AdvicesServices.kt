package com.muslims.firebasemvvm.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.muslims.firebasemvvm.models.Advice
import kotlinx.coroutines.tasks.await

object AdvicesServices {
    private const val TAG = "FirebaseAdvicesService"
    private const val COLLECTION_NAME = "Advices"

    suspend fun getAllAdvices(): ArrayList<Advice> {
        val db = FirebaseFirestore.getInstance()
        val advicesList = ArrayList<Advice>()
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    advicesList.add(document.toObject<Advice>())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }.await()
        return advicesList
    }

    suspend fun getAdviceById(adviceId :String): Advice?{
        val db = FirebaseFirestore.getInstance()
        var advice: Advice? = null
        val docRef = db.collection(COLLECTION_NAME).document(adviceId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    advice = document.toObject<Advice>()
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }.await()
        return advice
    }

    suspend fun addAdvice(advice: Advice){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(advice.id)
            .set(advice)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun deleteAdvice(adviceId:String){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(adviceId)
            .delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    suspend fun updateAdvice(advice: Advice){
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(advice.id)
            .set(advice)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

}