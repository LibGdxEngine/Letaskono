package com.muslims.firebasemvvm.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Advice(var id :String,
                  var url :String,
                  var time : String = Timestamp.now().toDate().toString()
                  ) : Parcelable
{
    constructor() : this(id = "" , url = "",time = Timestamp.now().toDate().toString())
}

