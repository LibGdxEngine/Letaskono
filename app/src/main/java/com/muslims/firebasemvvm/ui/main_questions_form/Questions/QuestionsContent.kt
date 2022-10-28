package com.muslims.firebasemvvm.ui.main_questions_form.Questions

import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.QuestionDataModel
import java.util.ArrayList
import java.util.HashMap

object QuestionsContent {
    private val TAG = this.javaClass.canonicalName

    fun items(type: String): MutableList<QuestionDataModel> {
        if (type == "man") {
            return MAN_QUESTIONS
        } else {
            WOMAN_QUESTIONS
        }
        return Questions
    }

    private var Questions = mutableListOf(
        QuestionDataModel.MCQ(
            question = "ما مدى التزامك بالصلاة ؟",
            answers = listOf(
                "أصلي جميع الصلوات في المسجد",
                "أصلي بعض الصلوات في المسجد وبعضها في المنزل",
                "أصلي جميع الصلوات في المنزل في وقتها",
                "متقطع في الصلاة",
                "لا أصلي والعياذ بالله",
            )
        ),
        QuestionDataModel.NumericInput(
            question = "ما هو سنك؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما هي جنسيتك؟",
        ),
        QuestionDataModel.NumericInput(
            question = "ما هو طولك؟",
        ),
        QuestionDataModel.NumericInput(
            question = "ما هو وزنك؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما هو مؤهلك التعليمي؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما هي وظيفتك؟",
        ),
        QuestionDataModel.TextInput(
            question = "إلى أي محافظة تنتمي/ـين في الأصل؟ وأين تعيش/ـين حاليا؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما هو عمل الوالد؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما هو عمل الوالدة؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما عدد الإخوة والأخوات وأعمارهم ومؤهلاتهم؟",
        ),
        QuestionDataModel.TextInput(
            question = "ما مقدار حفظك للقران؟",
        ),
        QuestionDataModel.MCQ(
            question = "هل تشاهد/ـي الأفلام او تستمع/ـين إلى الموسيقى أو الأغاني ؟",
            answers = listOf("لا أبدا", "نعم كثيرا", "نادرا")
        ),
        QuestionDataModel.TextInput(
            question = "هل لديك أي انتمائات دينية؟",
        ),
        QuestionDataModel.TextInput(
            question = "تكلم/ـي عن نفسك (أو ما يقوله الناس عنك)",
        ),
        QuestionDataModel.TextInput(
            question = "ما هي المواصفات التي تريدها في شريك/ـة حياتك؟",
        ),
    )

    private var MAN_QUESTIONS = mutableListOf<QuestionDataModel>(
        QuestionDataModel.MCQ(
            question = "الحالة الإجتماعية",
            answers = listOf("متزوج", "أرمل", "مطلق", "أعزب")
        ),
    )


    private var WOMAN_QUESTIONS = mutableListOf<QuestionDataModel>(
        QuestionDataModel.MCQ(
            question = "الحالة الإجتماعية",
            answers = listOf("عزباء", "أرملة", "مطلقة")
        ),
    )

    init {
        MAN_QUESTIONS.addAll(0, Questions)
        WOMAN_QUESTIONS.addAll(0, Questions)
    }

}