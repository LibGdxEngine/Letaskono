package com.muslims.firebasemvvm.ui.main_questions_form

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentQuestionsListBinding
import com.muslims.firebasemvvm.models.Question
import com.muslims.firebasemvvm.models.QuestionDataModel
import com.muslims.firebasemvvm.models.User

import com.muslims.firebasemvvm.ui.main_questions_form.Questions.QuestionsContent
import com.muslims.firebasemvvm.utils.DrawerLocker
import com.muslims.firebasemvvm.utils.StoredAuthUser


/**
 * A fragment representing a list of Items.
 */
class QuestionsFragment : Fragment(), QuestionsRvAdapter.Listener {

    private var columnCount = 1
    private var _binding: FragmentQuestionsListBinding? = null
    private lateinit var viewModel: QuestionsFragmentViewModel
    private var currentQuestionIndex = 0
    private var mToast: Toast? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var selectedGender: String
    private lateinit var phoneNumber: String
    private var currentUser: User? = null
    private var questionsList: MutableList<QuestionDataModel>? = null
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
        viewModel =
            ViewModelProvider(this).get(QuestionsFragmentViewModel::class.java)
        _binding = FragmentQuestionsListBinding.inflate(inflater, container, false)

        disableDrawer()

        selectedGender = arguments?.getString("gender").toString()
        phoneNumber = arguments?.getString("phone").toString()

        questionsList = QuestionsContent.items(selectedGender)



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

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            currentUser = user
        })

        viewModel.getUserStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                AuthenticationStatus.LOADING -> {
                    if (mToast != null) mToast?.cancel();
                    mToast = Toast.makeText(context, "LOADING", Toast.LENGTH_LONG);
                    mToast?.show();
                }
                AuthenticationStatus.DONE -> {
                    val questions = mutableListOf<Question>()
                    for (item in questionsList!!) {
                        var id: String? = null
                        var question: String? = null
                        var answer: String? = null
                        var note: String? = null
                        if (item is QuestionDataModel.MCQ) {
                            id = item.id
                            question = item.question
                            answer = item.selectedAnswer
                        } else if (item is QuestionDataModel.TextInput) {
                            id = item.id
                            question = item.question
                            answer = item.answer
                            note = item.note
                        } else if (item is QuestionDataModel.NumericInput) {
                            id = item.id
                            question = item.question
                            answer = item.answer
                            note = item.note
                        }
                        questions.add(Question(id!!, question!!, answer!!, note))
                    }
                    currentUser?.questionsList = questions
                    viewModel.updateUser(currentUser!!)
                }
                AuthenticationStatus.ERROR -> {
                    if (mToast != null) mToast?.cancel();
                    mToast = Toast.makeText(context, "ERROR", Toast.LENGTH_LONG);
                    mToast?.show();
                }
            }
        })

        viewModel.updatingStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                UpdatingStatus.LOADING -> {

                }
                UpdatingStatus.DONE -> {
                    StoredAuthUser.setUserInfoCompleted(requireContext(), true)
                    goToNextScreen()
                    if (mToast != null) mToast?.cancel();
                    mToast = Toast.makeText(context, "شكرا لك", Toast.LENGTH_LONG);
                    mToast?.show();
                }
                UpdatingStatus.ERROR -> {
                    if (mToast != null) mToast?.cancel();
                    mToast = Toast.makeText(context, "حصل خطأ ما...تواصل معنا لحله!", Toast.LENGTH_LONG);
                    mToast?.show();
                }
            }
        })

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
        if (currentQuestionIndex < questionsList?.size!! - 1) {//max number of questions
            val item = questionsList!![currentQuestionIndex]
            if (submittedAnswerIsValid(item)) {
                recyclerView.scrollToPosition(++currentQuestionIndex)
            } else {
                if (mToast != null) mToast?.cancel();
                mToast = Toast.makeText(context, "هذا السؤال ليس اختياريا!", Toast.LENGTH_LONG);
                mToast?.show();
            }
        } else {
            //finish questions and go to other page
            viewModel.getSignedInUser(phoneNumber)
        }
    }

    private fun goToNextScreen() {
//        val bundle = bundleOf("gender" to selectedGender)
        this.findNavController()
            .navigate(R.id.action_questionsFragment_to_navigation_home,
                null,
                navOptions {
                    anim {
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
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