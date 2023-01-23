package com.muslims.firebasemvvm.models
import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Report(var id :String,
                  var reporter:String,
                  var reported : String,
                  var body : String,
                  var time : String = Timestamp.now().toDate().toString()
) : Parcelable
{
    constructor() : this(
        id = "",
        reporter = "",
        body = "",
        reported = "",
        time = Timestamp.now().toDate().toString()
    )
}
