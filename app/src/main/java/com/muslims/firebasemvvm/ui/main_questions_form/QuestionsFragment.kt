package com.muslims.firebasemvvm.ui.main_questions_form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentQuestionsListBinding
import com.muslims.firebasemvvm.models.QuestionDataModel
import com.muslims.firebasemvvm.ui.main_questions_form.Questions.QuestionsContent
import com.muslims.firebasemvvm.utils.DrawerLocker


/**
 * A fragment representing a list of Items.
 */
class QuestionsFragment : Fragment(), QuestionsRvAdapter.Listener {

    private var columnCount = 1
    private var _binding: FragmentQuestionsListBinding? = null
    private var currentQuestionIndex = 0
    private var mToast: Toast? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedGender: String
    private val questionsRvAdapter: QuestionsRvAdapter by lazy {
        QuestionsRvAdapter(this@QuestionsFragment)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionsListBinding.inflate(inflater, container, false)

        disableDrawer()

        selectedGender = arguments?.getString("gender").toString()
        recyclerView = binding.list

        _binding!!.textViewLogo.setCharacterDelay(150)
        _binding!!.textViewLogo.animateText(getString(R.string.app_name));
        // Set the adapter
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> object : LinearLayoutManager(context) {
                        override fun canScrollVertically(): Boolean {
                            return false
                        }

                        override fun canScrollHorizontally(): Boolean {
                            return false
                        }
                    }
                    else -> GridLayoutManager(context, columnCount)
                }
                hasFixedSize()
                adapter = questionsRvAdapter
            }
        }
        questionsRvAdapter.setData(QuestionsContent.items(selectedGender))

        return binding.root
    }

    private fun disableDrawer() {
        (activity as DrawerLocker?)!!.setDrawerEnabled(false)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            QuestionsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    override fun onNextButtonClicked() {
        if (currentQuestionIndex < QuestionsContent.items(selectedGender).size - 1) {//max number of questions
            val item = QuestionsContent.items(selectedGender)[currentQuestionIndex]
            if (submittedAnswerIsValid(item)) {
                recyclerView.scrollToPosition(++currentQuestionIndex)
            } else {
                if (mToast != null) mToast?.cancel();
                mToast = Toast.makeText(context, "هذا السؤال ليس اختياريا!", Toast.LENGTH_LONG);
                mToast?.show();
            }


        } else {
            //finish questions and go to other page
            if (mToast != null) mToast?.cancel();
            mToast = Toast.makeText(context, "شكرا لك", Toast.LENGTH_LONG);
            mToast?.show();
        }
    }

    private fun submittedAnswerIsValid(item: QuestionDataModel): Boolean {
        var answerIsValid = false
        if (item is QuestionDataModel.MCQ) {
            answerIsValid = !item.selectedAnswer.toString().isNullOrEmpty()
        } else if (item is QuestionDataModel.TextInput) {
            answerIsValid = !item.answer.toString().isNullOrEmpty()
        } else if (item is QuestionDataModel.NumericInput) {
            answerIsValid = !item.answer.toString().isNullOrEmpty()
        }
        return answerIsValid
    }


    override fun onBackButtonClicked() {
        if (currentQuestionIndex >= 1) {
            recyclerView.scrollToPosition(--currentQuestionIndex)
        } else {
            if (mToast != null) mToast?.cancel();
            mToast = Toast.makeText(context, "لا يمكنك الرجوع أكثر", Toast.LENGTH_LONG);
            mToast?.show();
        }
    }

}