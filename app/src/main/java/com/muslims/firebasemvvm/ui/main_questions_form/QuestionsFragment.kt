package com.muslims.firebasemvvm.ui.main_questions_form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.databinding.FragmentQuestionsListBinding

import com.muslims.firebasemvvm.ui.main_questions_form.Questions.QuestionsContent
import com.muslims.firebasemvvm.utils.DrawerLocker


/**
 * A fragment representing a list of Items.
 */
class QuestionsFragment : Fragment() {

    private var columnCount = 1
    private var _binding: FragmentQuestionsListBinding? = null
    private var currentQuestionIndex = 0
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

        val recyclerView = binding.list
        // Set the adapter
        if (recyclerView is RecyclerView) {
            with(recyclerView) {
                layoutManager = when {
                    columnCount <= 1 -> object : LinearLayoutManager(context){
                        override fun canScrollVertically(): Boolean { return false }
                        override fun canScrollHorizontally(): Boolean { return false }
                    }
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyQuestionRecyclerViewAdapter(QuestionsContent.ITEMS)
            }
        }

        binding.nextBtn.setOnClickListener {
            if(currentQuestionIndex <= QuestionsContent.ITEMS.size)//max number of questions
                recyclerView.scrollToPosition(++currentQuestionIndex)
        }
        binding.backBtn.setOnClickListener {
            if(currentQuestionIndex >= 1)
                recyclerView.scrollToPosition(--currentQuestionIndex)
        }
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
}