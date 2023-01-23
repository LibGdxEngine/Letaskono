package com.muslims.firebasemvvm.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

enum class GENERAL_STATUS {
    WANT_MARRY, WANT_SEE, ALREADY_MARRIED
}

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var gender: String = "",
    var questionsList: List<Map<String, String>>? = null,
    var password: String = "",
    var phone: String = "",
    var favourites: String? = "",
    var acceptanceStatus: String = "pending",
    var generalStatus: String = GENERAL_STATUS.WANT_MARRY.toString(),
    var historyInfo: String = "",
    var relatedWith: String = "",
    @field:JvmField
    var isBlocked: Boolean = false,
    val profileCreationDate: String = Timestamp.now().toDate().toString(),
) : Parcelable {

    override fun toString(): String {
        return "عريس 25 سنة"
    }
}

