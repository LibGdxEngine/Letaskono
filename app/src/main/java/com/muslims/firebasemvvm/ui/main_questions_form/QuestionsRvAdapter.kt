package com.muslims.firebasemvvm.ui.main_questions_form

import android.content.Context
import android.content.res.ColorStateList
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.QuestionDataModel

class QuestionsRvAdapter(private var mListener: Listener) :
    RecyclerView.Adapter<QuestionsRvAdapter.DataAdapterViewHolder>() {

    private val adapterData = mutableListOf<QuestionDataModel>()

    interface Listener {
        fun onAnswerItemClick(index: Int, text: String, note: String?)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataAdapterViewHolder {
        val layout = when (viewType) {
            TYPE_MCQ -> R.layout.questions_row
            TYPE_TEXT_INPUT -> R.layout.question_row_text_input
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
    }

    inner class DataAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private fun bindMCQ(item: QuestionDataModel.MCQ) {
            val radioGroup = itemView.findViewById<RadioGroup>(R.id.answersRadioGroup)
            if (radioGroupIsEmpty(radioGroup)) {
                val question: Question = Question(
                    id = position.toString(),
                    questionText = item.question,
                    answers = item.answers
                )
                generateRadioButtons(question, radioGroup)
            }
            itemView.findViewById<TextView>(R.id.question_text).text = item.question
        }

        private fun generateRadioButtons(question: Question, radioGroup: RadioGroup?) {
            for (i in 1..question.answers.size) {
                val radioButton =
                    generateSingleRadioButton(
                        radioGroup!!.context,
                        i,
                        question.answers[i - 1]
                    )
                radioGroup?.addView(radioButton)
            }
        }

        private fun generateSingleRadioButton(
            context: Context,
            i: Int,
            answer: String
        ): RadioButton {
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
                    mListener?.onAnswerItemClick(i - 1, rb.text.toString(), null)
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
            val answerTextInput = itemView.findViewById<TextInputEditText>(R.id.answerTextInput)
            val noteTextInput = itemView.findViewById<TextInputEditText>(R.id.noteTextInput)
            itemView.findViewById<TextView>(R.id.question_text).text = item.question
            mListener?.onAnswerItemClick(position, answerTextInput.text.toString(), noteTextInput.text.toString())
        }

        fun bind(questionDataModel: QuestionDataModel) {
            when (questionDataModel) {
                is QuestionDataModel.MCQ -> bindMCQ(questionDataModel)
                is QuestionDataModel.TextInput -> bindTextInput(questionDataModel)
            }
        }
    }


}