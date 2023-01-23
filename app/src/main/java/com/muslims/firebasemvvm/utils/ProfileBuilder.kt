package com.muslims.firebasemvvm.utils

import com.muslims.firebasemvvm.models.QuestionDataModel
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.main_questions_form.Questions.QuestionsContent
import kotlin.reflect.typeOf

class ProfileBuilder {
    val manIds: MutableList<String> = mutableListOf(
        "1", "2", "3", "4", "5", "26", "6",
        "7", "8", "9", "10", "11", "12", "13",
        "18", "19", "21", "23", "25"
    )
    val womanIds: MutableList<String> = mutableListOf(
        "1", "2", "3", "4", "5", "26", "6",
        "7", "8", "9", "10", "11", "12", "13",
        "18", "22", "25", "24"
    )

    fun build(user: User): String {
        var result = ""
        result += if (user.gender == "man") "عريس: " else "عروسة:"
        result += "\n"
        result += "${user.id}"


        if (user.gender == "man") {
            for (id: String in manIds) {
                println(id)
                var question = user.questionsList?.first { it.get("id") == id }?.getOrDefault("questionText", "-1")
                if(question == "-1"){
                    question = user.questionsList?.first { it.get("id") == id }?.getOrDefault("question", " - ")
                }
                result += "\n${question}" +
                        ": " +
                        //answer
                        "${user.questionsList?.first { it.get("id") == id }?.get("answer")}" +
                        " " +
                        //note
                        if (user.questionsList?.first { it.get("id") == id }?.get("note")
                                .isNullOrEmpty()
                        ) "" else "(${
                            user.questionsList?.first { it.get("id") == id }?.get("note")
                        })"
            }
        } else {
            for (id: String in womanIds) {
                var question = user.questionsList?.first { it.get("id") == id }?.getOrDefault("questionText", "-1")
                if(question == "-1"){
                    question = user.questionsList?.first { it.get("id") == id }?.getOrDefault("question", " - ")
                }
                result += "\n${
                    question
                }" +
                        ": " +
                        //answer
                        "${
                            user.questionsList?.first { it.getOrDefault("id", "") == id }
                                ?.get("answer")
                        }" +
                        " " +
                        //note
                        if (user.questionsList?.first { it.getOrDefault("id", "") == id }
                                ?.get("note")
                                .isNullOrEmpty()
                        ) "" else "(${
                            user.questionsList?.first { it.getOrDefault("id", "") == id }
                                ?.get("note")
                        })"
                if (id == "26") {
                    var question = user.questionsList?.first { it.get("id") == id }?.getOrDefault("questionText", "-1")
                    if(question == "-1"){
                        question = user.questionsList?.first { it.get("id") == id }?.getOrDefault("question", " - ")
                    }
                    result += "\n${
                        question
                    }" +
                            ": " +
                            //answer
                            "${
                                user.questionsList?.filter { it.get("id") == "26" }?.get(1)
                                    ?.get("answer")
                            }" +
                            " " +
                            //note
                            if (user.questionsList?.filter { it.get("id") == "26" }?.get(1)
                                    ?.get("note")
                                    .isNullOrEmpty()
                            ) "" else "(${
                                user.questionsList?.filter { it.get("id") == "26" }?.get(1)
                                    ?.get("note")
                            })"
                }
            }


        }


        return result
    }
}