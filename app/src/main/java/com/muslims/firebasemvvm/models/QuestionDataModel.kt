package com.muslims.firebasemvvm.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class QuestionDataModel : Parcelable {
    @Parcelize
    data class MCQ(
        val id:String,
        val question: String,
        val answers: List<String>,
        var selectedAnswer: String? = ""
    ) : QuestionDataModel()
    @Parcelize
    data class TextInput(
        val id:String,
        val question: String,
        var answer: String? = "",
        var note: String? = ""
    ) : QuestionDataModel()
    @Parcelize
    data class NumericInput(
        val id:String,
        val question: String,
        var answer: String? = "",
        var note: String? = ""
    ) : QuestionDataModel()
}
