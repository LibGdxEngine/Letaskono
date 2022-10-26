package com.muslims.firebasemvvm.models

sealed class QuestionDataModel {
    data class MCQ(
        val question: String,
        val answers: List<String>,
        var selectedAnswer: String? = ""
    ) : QuestionDataModel()
    data class TextInput(
        val question: String,
        var answer: String? = "",
        var note: String? = ""
    ) : QuestionDataModel()
}
