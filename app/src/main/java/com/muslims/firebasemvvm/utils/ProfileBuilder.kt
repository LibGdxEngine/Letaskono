package com.muslims.firebasemvvm.utils

import com.muslims.firebasemvvm.models.User

class ProfileBuilder {
    fun build(user: User): String {
        var result = ""
        result += if (user.gender == "man") "عريس: " else "عروسة:"
        result += "\n"
        for (i in 0..user.questionsList?.size!!-1) {
            //question
            result += "\n${user.questionsList?.get(i)?.questionText}" +
                    ": " +
                    //answer
                    "${user.questionsList?.get(i)?.answer}" +
                    " " +
                    //note
                    if (user.questionsList?.get(i)?.note.isNullOrEmpty()) "" else "(${
                        user.questionsList?.get(
                            i
                        )?.note
                    })"
        }

        return result
    }
}