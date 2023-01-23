package com.muslims.firebasemvvm.ui.main_questions_form.Questions

import com.google.android.material.textfield.TextInputEditText


import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.QuestionDataModel
import kotlinx.coroutines.flow.merge
import java.util.ArrayList
import java.util.HashMap

object QuestionsContent {
    ///////////////////////////////////////////////////////////////////////////
    // #ملحوظة_هامة_من_إدارة_الصفحة 📢📢
    //جميع البيانات المذكورة تحت مسؤلية العروسة أو من ينوب عنها فى تقديم الاستمارة 👌
    //و الصفحه غير مسؤله عن أى تعارض فيما تم ذكره و لكم أن تتأكدوا  بأنفسكم من صحه البيانات و صدق ما جاء فيها دون أدنى مسؤوليه تقع على إدارة الصفحة أو فريق العمل
    ///////////////////////////////////////////////////////////////////////////
    private val TAG = this.javaClass.canonicalName

    fun items(type: String): MutableList<QuestionDataModel> {
        return if (type == "man") {
            MAN_QUESTIONS
        } else {
            WOMAN_QUESTIONS
        }
    }


    private var MAN_QUESTIONS = mutableListOf<QuestionDataModel>(
        QuestionDataModel.MCQ(
            id = "1",
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
            id = "2",
            question = "ما هو سنك؟",
        ),
        QuestionDataModel.TextInput(
            id = "3",
            question = "ما هي جنسيتك؟",
        ),

        QuestionDataModel.TextInput(
            id = "4",
            question = "تكلم عن نفسك (أو ما يقوله الناس عنك)",
        ),
        QuestionDataModel.TextInput(
            id = "5",
            question = "ما هي المواصفات التي تريدها في زوجتك ؟",
        ),
        QuestionDataModel.NumericInput(
            id = "26",
            question = "ما هو طولك؟",
        ),
        QuestionDataModel.NumericInput(
            id = "6",
            question = "ما هو وزنك؟",
        ),
        QuestionDataModel.TextInput(
            id = "7",
            question = "ما هو لون بشرتك؟",
        ),
        QuestionDataModel.TextInput(
            id = "8",
            question = "ما هو مؤهلك التعليمي؟",
        ),
        QuestionDataModel.TextInput(
            id = "9",
            question = "ما هي وظيفتك؟",
        ),
        QuestionDataModel.TextInput(
            id = "10",
            question = "هل تعاني من أي أمراض أو إعاقات؟",
        ),
        QuestionDataModel.TextInput(
            id = "11",
            question = "إلى أي محافظة تنتمي في الأصل؟ وأين تعيش حاليا؟",
        ),
        QuestionDataModel.TextInput(
            id = "26",
            question = "اذكر اسم المركز داخل المحافظة",
        ),
        QuestionDataModel.TextInput(
            id = "12",
            question = "ما هو عمل الوالد؟",
        ),
        QuestionDataModel.TextInput(
            id = "13",
            question = "ما هو عمل الوالدة؟",
        ),
//        QuestionDataModel.TextInput(
//            id = "14",
//            question = "ما عدد الإخوة والأخوات وأعمارهم ومؤهلاتهم؟",
//        ),
//        QuestionDataModel.TextInput(
//            id = "15",
//            question = "ما مقدار حفظك للقران؟",
//        ),
//        QuestionDataModel.MCQ(
//            id = "16",
//            question = "هل تشاهد الأفلام او تستمع إلى الموسيقى أو الأغاني ؟",
//            answers = listOf("لا", "نعم كثيرا", "نادرا")
//        ),
//        QuestionDataModel.TextInput(
//            id = "17",
//            question = "هل لديك أي انتمائات دينية؟",
//        ),
        QuestionDataModel.TextInput(
            id = "18",
            question = "من مرسل الاستمارة ،أنت أم أحد معارفك؟",
        ),
        QuestionDataModel.TextInput(
            id = "25",
            question = "هل لديك أولاد وما عددهم؟"
        ),
        QuestionDataModel.MCQ(
            id = "23",
            question = "هل أنت ملتحي؟",
            answers = listOf("نعم", "لحية خفيفة", "لا")
        ),
//        QuestionDataModel.TextInput(
//            id = "20",
//            question = "هل أنت رياضي؟",
//        ),
        QuestionDataModel.TextInput(
            id = "21",
            question = "هل أنت مدخن؟",
        ),
        QuestionDataModel.MCQ(
            id = "19",
            question = "ما هي حالتك الإجتماعية؟",
            answers = listOf("متزوج", "أرمل", "مطلق", "أعزب")
        ),
    )


    private var WOMAN_QUESTIONS = mutableListOf<QuestionDataModel>(
        QuestionDataModel.MCQ(
            id = "1",
            question = "ما مدى التزامك بالصلاة ؟",
            answers = listOf(
                "أصلي جميع الصلوات في المنزل في وقتها",
                "متقطعة في الصلاة",
                "لا أصلي والعياذ بالله",
            )
        ),
        QuestionDataModel.NumericInput(
            id = "2",
            question = "ما هو سنك؟",
        ),
        QuestionDataModel.TextInput(
            id = "3",
            question = "ما هي جنسيتك؟",
        ),
        QuestionDataModel.TextInput(
            id = "4",
            question = "تكلمي عن نفسك (أو ما يقوله الناس عنك)",
        ),
        QuestionDataModel.TextInput(
            id = "5",
            question = "ما هي المواصفات التي تريدينها في زوجك؟",
        ),
        QuestionDataModel.NumericInput(
            id = "26",
            question = "ما هو طولك؟",
        ),
        QuestionDataModel.NumericInput(
            id = "6",
            question = "ما هو وزنك؟",
        ),
        QuestionDataModel.TextInput(
            id = "7",
            question = "ما هو لون بشرتك؟",
        ),
        QuestionDataModel.TextInput(
            id = "8",
            question = "ما هو مؤهلك التعليمي؟",
        ),
        QuestionDataModel.TextInput(
            id = "9",
            question = "ما هي وظيفتك؟",
        ),
        QuestionDataModel.TextInput(
            id = "10",
            question = "هل تعانين من أي أمراض أو إعاقات؟",
        ),
        QuestionDataModel.TextInput(
            id = "11",
            question = "إلى أي محافظة تنتمين في الأصل؟ وأين تعيشين حاليا؟",
        ),
        QuestionDataModel.TextInput(
            id = "26",
            question = "اذكري اسم المركز داخل المحافظة",
        ),
        QuestionDataModel.TextInput(
            id = "12",
            question = "ما هو عمل الوالد؟",
        ),
        QuestionDataModel.TextInput(
            id = "13",
            question = "ما هو عمل الوالدة؟",
        ),
//        QuestionDataModel.TextInput(
//            id = "14",
//            question = "ما عدد الإخوة والأخوات وأعمارهم ومؤهلاتهم؟",
//        ),
//        QuestionDataModel.TextInput(
//            id = "15",
//            question = "ما مقدار حفظك للقران؟",
//        ),
//        QuestionDataModel.MCQ(
//            id = "16",
//            question = "هل تشاهدين الأفلام او تستمعين إلى الموسيقى أو الأغاني ؟",
//            answers = listOf("لا", "نعم كثيرا", "نادرا")
//        ),
//        QuestionDataModel.TextInput(
//            id = "17",
//            question = "هل لديك أي انتمائات دينية؟",
//        ),
        QuestionDataModel.TextInput(
            id = "18",
            question = "من مرسل الاستمارة ،أنت أم أحد معارفك؟",
        ),
        QuestionDataModel.TextInput(
            id = "25",
            question = "هل لديك أولاد وما عددهم؟"
        ),
        QuestionDataModel.MCQ(
            id = "24",
            question = "ما هو شكل حجابك؟",
            answers = listOf("منتقبة", "مختمرة", "طرح وفساتين", "طرح وبناطيل")
        ),
        QuestionDataModel.MCQ(
            id = "26",
            question = "هل لديك استعداد للنقاب؟",
            answers = listOf("منتقبة بالفعل", "نعم أتمنى", "لا")
        ),
        QuestionDataModel.MCQ(
            id = "22",
            question = "الحالة الإجتماعية",
            answers = listOf("عزباء", "أرملة", "مطلقة")
        ),
        QuestionDataModel.TextInput(
            id = "26",
            question = "اذا كانت لديك اي مشكلة مع ولي أمرك بخصوص مسألة الزواج فيرجى شرح المشكلة لنا حتى نساعدك في حلها"
        ),
        QuestionDataModel.NumericInput(
            id = "6",
            question = "يرجى ادخال رقم هاتف ولي الأمر مع العلم اننا لن نتواصل معه الا بعد الرجوع اليك",
        ),
    )

}