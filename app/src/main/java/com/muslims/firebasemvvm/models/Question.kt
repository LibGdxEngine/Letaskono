package com.muslims.firebasemvvm.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(var id: String = "",
                    var questionText: String = "",
                    var answer: Answer? = null) : Parcelable