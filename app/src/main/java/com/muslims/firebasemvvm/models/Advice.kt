package com.muslims.firebasemvvm.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Advice(var id :String,
                  var url :String,
                  var title : String,
                  var body : String,
                  var category : String,
                  var time : String = Timestamp.now().toDate().toString()
                  ) : Parcelable
{
    constructor() : this(
        id = "",
        url = "",
        body = "",
        title = "",
        category = "",
        time = Timestamp.now().toDate().toString()
    )
}

