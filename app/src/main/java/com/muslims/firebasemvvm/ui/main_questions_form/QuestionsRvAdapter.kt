package com.muslims.firebasemvvm.ui.main_questions_form

import android.content.Context
import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.QuestionDataModel


class QuestionsRvAdapter(private var mListener: Listener) :
    RecyclerView.Adapter<QuestionsRvAdapter.DataAdapterViewHolder>() {

    private val adapterData = mutableListOf<QuestionDataModel>()

    interface Listener {
        fun onNextButtonClicked()
        fun onBackButtonClicked()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            TYPE_MCQ -> R.layout.questions_row
            TYPE_TEXT_INPUT -> R.layout.question_row_text_input
            TYPE_NUMERIC_INPUT -> R.layout.question_row_numeric_input
            else -> throw IllegalArgumentException("Invalid type")
        }

        val view = LayoutInflater
            .from(parent.context)
            .inflate(layout, parent, false)

        return DataAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DataAdapterViewHolder, position: Int) {
        holder.bind(adapterData[position])
    }

    override fun getItemCount(): Int = adapterData.size

    override fun getItemViewType(position: Int): Int {
        return when (adapterData[position]) {
            is QuestionDataModel.MCQ -> TYPE_MCQ
            is QuestionDataModel.TextInput -> TYPE_TEXT_INPUT
            is QuestionDataModel.NumericInput -> TYPE_NUMERIC_INPUT
        }
    }

    fun setData(data: List<QuestionDataModel>) {
        adapterData.apply {
            clear()
            addAll(data)
        }
    }

    companion object {
        private const val TYPE_MCQ = 0
        private const val TYPE_TEXT_INPUT = 1
        private const val TYPE_NUMERIC_INPUT = 2
    }

    inner class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var questionText: TextView? = null
        private fun bindMCQ(item: QuestionDataModel.MCQ) {
            setIsRecyclable(true)
            val radioGroup = itemView.findViewById<RadioGroup>(R.id.answersRadioGroup)
            val nextButton = itemView.findViewById<Button>(R.id.nextBtn).apply {
                setOnClickListener {
                    mListener.onNextButtonClicked()
                }
            }
            val backButton = itemView.findViewById<Button>(R.id.backBtn).apply {
                setOnClickListener {
                    mListener.onBackButtonClicked()
                }
            }

            if (radioGroupIsEmpty(radioGroup)) {
                val question: ItemQuestion = ItemQuestion(
                    id = position.toString(),
                    questionText = item.question,
                    answers = item.answers
                )
                generateRadioButtons(question, radioGroup, item.selectedAnswer)
            }

            questionText = itemView.findViewById<TextView>(R.id.question_text).apply {
                text = item.question
            }
        }

        inner class ItemQuestion(var id:String,var questionText:String, var answers:List<String>)

        private fun generateRadioButtons(
            question: ItemQuestion,
            radioGroup: RadioGroup?,
            selectedAnswer: String?
        ) {
            for (i in 1..question.answers.size) {
                val radioButton =
                    generateSingleRadioButton(
                        radioGroup!!.context,
                        i,
                        question.answers[i - 1],
                        selectedAnswer
                    )

                radioGroup.apply {
                    addView(radioButton)
                }
            }
        }

        private fun generateSingleRadioButton(
            context: Context,
            i: Int,
            answer: String,
            selectedAnswer: String?
        ): RadioButton {
            val selectedBtn = selectedAnswer == answer
            val radio = RadioButton(context).apply {
                layoutDirection = LayoutDirection.RTL
                id = i
                text = answer.toString()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    140
                )
                setPadding(20, 0, 18, 0)
                background = resources.getDrawable(R.drawable.radio_btn_backround)
                buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_400))
                setOnClickListener {
                    var rb = it as RadioButton
                    var item = adapterData[position]
                    if (item is QuestionDataModel.MCQ) {
                        item.selectedAnswer = "${rb.text}"
                        questionText?.background =
                            resources.getDrawable(R.drawable.question_text_background_active)
                    }
                }
                if (selectedBtn) {
                    isChecked = true
                }

            }
            if (radio.getParent() != null) {
                (radio.getParent() as ViewGroup).removeView(radio) // <- fix
            }
            return radio
        }

        private fun radioGroupIsEmpty(radioGroup: RadioGroup?): Boolean {
            return radioGroup?.getChildAt(0) == null
        }

        private fun bindTextInput(item: QuestionDataModel.TextInput) {
            setIsRecyclable(false)
            val answerTextInput = itemView.findViewById<EditText>(R.id.answerTextInput)
            val noteTextInput = itemView.findViewById<EditText>(R.id.noteTextInput)
            val nextButton = itemView.findViewById<Button>(R.id.nextBtn).apply {
                setOnClickListener {
                    mListener.onNextButtonClicked()
                }
            }
            val backButton = itemView.findViewById<Button>(R.id.backBtn).apply {
                setOnClickListener {
                    mListener.onBackButtonClicked()
                }
            }

            questionText = itemView.findViewById<TextView>(R.id.question_text).apply {
                text = item.question
            }
            answerTextInput.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        var item = adapterData[position]
                        if (item is QuestionDataModel.TextInput) {
                            item.answer = s.toString()
                            if (item.answer!!.isNotEmpty()) {
                                questionText?.background =
                                    resources.getDrawable(R.drawable.question_text_background_active)
                            }
                            adapterData[position] = item
                        }
                    }
                })
                if (item.answer!!.isNotEmpty()) {
                    this.setText(item.answer)
                    questionText?.background =
                        resources.getDrawable(R.drawable.question_text_background_active)
                }
            }
            noteTextInput.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        var item = adapterData[position]
                        if (item is QuestionDataModel.TextInput) {
                            item.note = s.toString()
                            if (item.note!!.isNotEmpty()) {
                                questionText?.background =
                                    resources.getDrawable(R.drawable.question_text_background_active)
                            }
                            adapterData[position] = item
                        }
                    }
                })
                if (item.note!!.isNotEmpty()) {
                    this.setText(item.note)
                    questionText?.background =
                        resources.getDrawable(R.drawable.question_text_background_active)
                }
            }
        }

        private fun bindNumericInput(item: QuestionDataModel.NumericInput) {
            val answerTextInput = itemView.findViewById<EditText>(R.id.answerTextInput)
            val noteTextInput = itemView.findViewById<EditText>(R.id.noteTextInput)
            setIsRecyclable(false)
            val nextButton = itemView.findViewById<Button>(R.id.nextBtn).apply {
                setOnClickListener {
                    mListener.onNextButtonClicked()
                }
            }
            val backButton = itemView.findViewById<Button>(R.id.backBtn).apply {
                setOnClickListener {
                    mListener.onBackButtonClicked()
                }
            }

            questionText = itemView.findViewById<TextView>(R.id.question_text).apply {
                text = item.question
            }
            answerTextInput.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        var item = adapterData[position]
                        if (item is QuestionDataModel.NumericInput) {
                            item.answer = s.toString()
                            if (item.answer!!.isNotEmpty()) {
                                questionText?.background =
                                    resources.getDrawable(R.drawable.question_text_background_active)
                            }
                            adapterData[position] = item
                        }
                    }
                })
                if (item.answer!!.isNotEmpty()) {
                    this.setText(item.answer)
                    questionText?.background =
                        resources.getDrawable(R.drawable.question_text_background_active)
                }
            }
            noteTextInput.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable) {}
                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        var item = adapterData[position]
                        if (item is QuestionDataModel.NumericInput) {
                            item.note = s.toString()
                            if (item.note!!.isNotEmpty()) {
                                questionText?.background =
                                    resources.getDrawable(R.drawable.question_text_background_active)
                            }
                            adapterData[position] = item
                        }
                    }
                })
                if (item.note!!.isNotEmpty()) {
                    this.setText(item.note)
                    questionText?.background =
                        resources.getDrawable(R.drawable.question_text_background_active)
                }
            }
        }

        fun bind(questionDataModel: QuestionDataModel) {
            when (questionDataModel) {
                is QuestionDataModel.MCQ -> bindMCQ(questionDataModel)
                is QuestionDataModel.TextInput -> bindTextInput(questionDataModel)
                is QuestionDataModel.NumericInput -> bindNumericInput(questionDataModel)
            }
        }
    }


}