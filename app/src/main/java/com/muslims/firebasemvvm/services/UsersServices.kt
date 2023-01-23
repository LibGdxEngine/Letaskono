package com.muslims.firebasemvvm.services

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.mikepenz.materialdrawer.util.otherwise
import com.muslims.firebasemvvm.models.GENERAL_STATUS
import com.muslims.firebasemvvm.models.Report
import com.muslims.firebasemvvm.models.User
import kotlinx.coroutines.tasks.await


object UsersServices {
    private const val TAG = "FirebaseUsersService"
    private const val COLLECTION_NAME = "Users"
    private const val COLLECTION_REPORTS = "REPORTS"

    suspend fun getAllUsers(): ArrayList<User> {
        val db = FirebaseFirestore.getInstance()
        val usersList = ArrayList<User>()
        db.collection(COLLECTION_NAME)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
                    usersList.add(document.toObject<User>())
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting user details")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }.await()
        return usersList
    }

    suspend fun unrelate(user: User) {
        val otherUser = getUserById(user.relatedWith)
        user.relatedWith = ""
        user.generalStatus = GENERAL_STATUS.WANT_MARRY.toString()
        otherUser?.generalStatus = GENERAL_STATUS.WANT_MARRY.toString()
        otherUser?.relatedWith = ""

        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.phone)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        db.collection(COLLECTION_NAME)
            .document(otherUser!!.phone)
            .set(otherUser)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun marry(user: User, otherUser: User): Boolean {
        if (user.gender == otherUser.gender) {
            return false
        }
        user.relatedWith = otherUser.id
        user.generalStatus = GENERAL_STATUS.ALREADY_MARRIED.toString()

        otherUser.relatedWith = user.id
        otherUser.generalStatus = GENERAL_STATUS.ALREADY_MARRIED.toString()

        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.phone)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        db.collection(COLLECTION_NAME)
            .document(otherUser.phone)
            .set(otherUser)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        return true
    }

    suspend fun relate(user: User, otherUser: User): String {

        if (user.gender == otherUser.gender) {
            return "SAME_GENDER"
        }

        if (otherUser.relatedWith != "") {
            if (otherUser.generalStatus == GENERAL_STATUS.WANT_SEE.toString()) {
                return "ALREADY_RELATED"
            } else if (otherUser.generalStatus == GENERAL_STATUS.ALREADY_MARRIED.toString())
                return "ALREADY_MARRIED"
        }

        user.relatedWith = otherUser.id
        user.generalStatus = GENERAL_STATUS.WANT_SEE.toString()

        otherUser.relatedWith = user.id
        otherUser.generalStatus = GENERAL_STATUS.WANT_SEE.toString()

        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.phone)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        db.collection(COLLECTION_NAME)
            .document(otherUser.phone)
            .set(otherUser)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        return "DONE"
    }

    suspend fun getUserByPhoneNumber(phone: String): User? {
        val db = FirebaseFirestore.getInstance()
        var user: User? = null
        val docRef = db.collection(COLLECTION_NAME).document(phone)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    user = document.toObject<User>()
                } else {
                    Log.d(TAG, "No such User")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting user details")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }.await()
        return user
    }


    suspend fun getUserByMobile(phone: String): User? {
        val db = FirebaseFirestore.getInstance()
        var user: User? = null
        var docRef = db.collection(COLLECTION_NAME)
            .whereEqualTo("phone", phone)

        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        user = document.toObject<User>()
                    } else {
                        Log.d(TAG, "No such User here")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting user details")
                FirebaseCrashlytics.getInstance().recordException(exception)

            }.await()
        return user
    }

    suspend fun getUserByCode(code: String, currentUserGender: String): User? {
        val db = FirebaseFirestore.getInstance()
        var user: User? = null
        var docRef = db.collection(COLLECTION_NAME)
            .whereEqualTo("id", code)
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        user = document.toObject<User>()
                    } else {
                        Log.d(TAG, "No such User here")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting user details")
                FirebaseCrashlytics.getInstance().recordException(exception)

            }.await()
        if (user != null) {
            if (user?.gender == currentUserGender) {
                return null
            }
        }
        return user
    }

    suspend fun getUserById(userId: String): User? {
        val db = FirebaseFirestore.getInstance()
        var user: User? = null
        val docRef = db.collection(COLLECTION_NAME)
            .whereEqualTo("id", userId)

        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                        user = document.toObject<User>()
                    } else {
                        Log.d(TAG, "No such User")
                    }
                }

            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                FirebaseCrashlytics.getInstance().log("Error getting user details")
                FirebaseCrashlytics.getInstance().recordException(exception)
            }.await()
        return user
    }


    suspend fun addUser(user: User): Boolean {
        var userAdded = true
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.phone)
            .set(user)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                userAdded = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                FirebaseCrashlytics.getInstance().log("Error getting user details")
                FirebaseCrashlytics.getInstance().recordException(e)
                userAdded = false
            }
        return userAdded
    }

    suspend fun deleteUser(userId: String) {

    }

    suspend fun updateUser(user: User) {
        val db = FirebaseFirestore.getInstance()
        db.collection(COLLECTION_NAME)
            .document(user.phone)
            .set(user)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun addToFavourite(user: User, favourite: String) {
        val db = FirebaseFirestore.getInstance()
        val sfDocRef = db.collection(COLLECTION_NAME).document(user.phone)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)

            // Note: this could be done without a transaction
            //       by updating the population using FieldValue.increment()
            val newFavourites = snapshot.getString("favourites")!! + favourite + ","
            transaction.update(sfDocRef, "favourites", newFavourites)
            // Success
            null
        }.addOnSuccessListener { Log.d(TAG, "Transaction success!") }
            .addOnFailureListener { e -> Log.w(TAG, "Transaction failure.", e) }

    }

    suspend fun report(reported: User, reporter: User) {
        val db = FirebaseFirestore.getInstance()
        val report = Report(
            id = "${reporter.phone + " - " + reported.phone}",
            reported = reported.phone,
            reporter = reporter.phone,
            body = "",
            time = Timestamp.now().toDate().toString(),
        )
        db.collection(COLLECTION_REPORTS)
            .document(reported.phone)
            .set(report)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    suspend fun removeFromFavourite(user: User, favourite: String) {
        val db = FirebaseFirestore.getInstance()
        val sfDocRef = db.collection(COLLECTION_NAME).document(user.phone)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)

            // Note: this could be done without a transaction
            //       by updating the population using FieldValue.increment()
            Log.w(TAG, "Hello : ${snapshot.getString("id")}")
            val removedFavourites = snapshot.getString("favourites")!!.split(",").toMutableList()

            removedFavourites.remove(favourite)
            val string = removedFavourites.joinToString(",")
            transaction.update(sfDocRef, "favourites", string)
            // Success
            null
        }.addOnSuccessListener { Log.d(TAG, "Transaction success!") }
            .addOnFailureListener { e -> Log.w(TAG, "Transaction failure.", e) }

    }

    suspend fun block(user: User) {
        val db = FirebaseFirestore.getInstance()
        val sfDocRef = db.collection(COLLECTION_NAME).document(user.phone)
        db.runTransaction { transaction ->
            val snapshot = transaction.get(sfDocRef)

            transaction.update(sfDocRef, "isBlocked", !user.isBlocked)
            // Success
            null
        }.addOnSuccessListener { Log.d(TAG, "Transaction success!") }
            .addOnFailureListener { e -> Log.w(TAG, "Transaction failure.", e) }
    }
}