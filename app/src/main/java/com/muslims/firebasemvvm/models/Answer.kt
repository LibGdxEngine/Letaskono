package com.muslims.firebasemvvm.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Answer(val answerText: String) : Parcelable{
    override fun toString(): String {
        return this.answerText
    }
}