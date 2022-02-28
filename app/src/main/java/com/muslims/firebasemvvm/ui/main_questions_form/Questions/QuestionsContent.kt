package com.muslims.firebasemvvm.ui.main_questions_form.Questions

import com.muslims.firebasemvvm.models.Answer
import com.muslims.firebasemvvm.models.Question
import java.util.ArrayList
import java.util.HashMap

object QuestionsContent {
    private val TAG = this.javaClass.canonicalName

    private val QUESTIONS_TEXT = listOf<String>(
        "ما مدى التزامك بالصلاة ؟",
        "هل تشاهد الأفللام او تستمع إلى الموسيقى ؟",
        "هل أنت ملتحي ؟",
        "ما هو نوع حجابك ؟"
    )

    private val ANSWERS_TEXT = hashMapOf(
        QUESTIONS_TEXT[0] to listOf<String>(
            "أصلي جميع الصلوات في المسجد",
            "أصلي بعض الصلوات في المسجد وبعضها في المنزل",
            "أصلي جميع الصلوات في المنزل في وقتها",
            "متقطع في الصلاة",
            "لا أصلي والعياذ بالله",
        ),
        QUESTIONS_TEXT[1] to listOf("لا أبدا", "نعم كثيرا", "نادرا"),
        QUESTIONS_TEXT[2] to listOf("نعم", "لحية خفيفة", "لا لست ملتحيا"),
        QUESTIONS_TEXT[3] to listOf("منتقبة", "خمار", "طرحة", "غير محجبة"),
    )

    val ITEMS: MutableList<Question> = ArrayList()

    val ITEM_MAP: MutableMap<String, Question> = HashMap()

    private val COUNT = QUESTIONS_TEXT.size

    init {
        for (i in 1..COUNT) {
            addItem(createQuestionItem(i))
        }
    }

    private fun addItem(item: Question) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createQuestionItem(position: Int): Question {
        val questionText = QUESTIONS_TEXT[position - 1]
        return Question(
            position.toString(),
            questionText,
            answersListOf(questionText)
        )
    }


    private fun answersListOf(questionText: String): List<Answer> {
        val answers = mutableListOf<Answer>()
        val questionAnswers = ANSWERS_TEXT[questionText]
        for (i in 1..questionAnswers!!.size) {
            answers.add(Answer(questionAnswers[i-1]))
        }
        return answers
    }

}