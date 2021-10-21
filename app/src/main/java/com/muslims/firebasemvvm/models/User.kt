package com.muslims.firebasemvvm.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var name:String = "Mohammed",
                var id:String = "-1",
                var sex:String = "Male",
                var age:String = "63",
                var height:String = "180",
                var skinColor:String = "White",
                var city:String = "Makka",
                var weight:String = "90",
                var previousRelation:Relation = Relation.MARRY,
                var wantChildren:String = "Yes",
                var certificate:String = "Highest",
                @field:JvmField
                var isBlocked:Boolean = false,
                val profileCreationDate:String = Timestamp.now().toDate().toString(),
                val lastProfileUpdate:String = Timestamp.now().toDate().toString(),
                ) : Parcelable{
    //Types of previous-relations
    enum class Relation {
        NO, MARRY, ENGAGEMENT
    }
}

