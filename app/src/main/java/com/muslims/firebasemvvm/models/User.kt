package com.muslims.firebasemvvm.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String = "Mohammed",
    var gender:String = "",
    var questionsList: List<Question>? = null,
    var password: String = "",
    var phone: String = "",
    @field:JvmField
    var isBlocked: Boolean = false,
    val profileCreationDate: String = Timestamp.now().toDate().toString(),
    val lastProfileUpdate: String = Timestamp.now().toDate().toString(),
) : Parcelable

