package com.muslims.firebasemvvm.ui.regesteration_questions_form

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.LayoutDirection
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.marginTop
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.QuestionsRowBinding
import com.muslims.firebasemvvm.ui.regesteration_questions_form.placeholder.PlaceholderContent.PlaceholderItem


class MyQuestionRecyclerViewAdapter(
    private val values: List<PlaceholderItem>
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


        for (i in 1..4){
            val radioButton = getRadioButton(holder, i)
            if (radioButton.getParent() != null) {
                (radioButton.getParent() as ViewGroup).removeView(radioButton) // <- fix
            }
            holder.answersRadioGroup.addView(radioButton);
        }
        holder.idView.text = item.id
    }

    private fun getRadioButton(
        holder: ViewHolder,
        i: Int
    ) : RadioButton {
        val radio = RadioButton(holder.idView.context).apply {
            layoutDirection = LayoutDirection.RTL
            id = i
            text = i.toString()
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                140
            )
            setPadding(20, 0, 18, 0)
            background = resources.getDrawable(R.drawable.radio_btn_backround)
            buttonTintList = ColorStateList.valueOf(resources.getColor(R.color.blue_400))
        }
        return radio
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: QuestionsRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.questionText
        val answersRadioGroup: RadioGroup = binding.answersRadioGroup
//        val contentView: TextView = binding.content

//        override fun toString(): String {
////            return super.toString() + " '" + contentView.text + "'"
//        }
    }

}