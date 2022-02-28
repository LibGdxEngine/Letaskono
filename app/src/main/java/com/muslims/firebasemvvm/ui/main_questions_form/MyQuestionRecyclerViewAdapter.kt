package com.muslims.firebasemvvm.ui.main_questions_form

import android.content.Context
import android.content.res.ColorStateList
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.QuestionsRowBinding
import com.muslims.firebasemvvm.models.Answer
import com.muslims.firebasemvvm.models.Question


class MyQuestionRecyclerViewAdapter(
    private val values: List<Question>
) : RecyclerView.Adapter<MyQuestionRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            QuestionsRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.setIsRecyclable(false)
        if(radioGroupIsEmpty(holder)) {
            generateRadioButtons(item, holder)
        }
        holder.questionTextView.text = item.questionText
    }

    private fun generateRadioButtons(
        item: Question,
        holder: ViewHolder
    ) {
        for (i in 1..item.answers.size) {
            val radioButton = generateSingleRadioButton(holder.answersRadioGroup.context, i, item.answers[i-1])
            holder.answersRadioGroup.addView(radioButton);
        }
    }

    private fun radioGroupIsEmpty(holder: ViewHolder) =
        holder.answersRadioGroup.getChildAt(0) == null

    private fun generateSingleRadioButton(
        context: Context,
        i: Int,
        answer: Answer
    ) : RadioButton {
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
        }
        if (radio.getParent() != null) {
            (radio.getParent() as ViewGroup).removeView(radio) // <- fix
        }
        return radio
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: QuestionsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val questionTextView: TextView = binding.questionText
        val answersRadioGroup: RadioGroup = binding.answersRadioGroup
    }

}