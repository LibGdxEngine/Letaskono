package com.muslims.firebasemvvm.ui.login_registeration.ui.register


import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.material.textfield.TextInputEditText
import com.muslims.firebasemvvm.HomeActivity
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.RegisterationFragmentBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.main_questions_form.Questions.QuestionsContent
import com.muslims.firebasemvvm.utils.AuthenticatedUser
import com.muslims.firebasemvvm.utils.StoredAuthUser
import kotlin.random.Random


class RegistrationFragment : Fragment() {

    private lateinit var viewModel: RegistrationViewModel
    private var _binding: RegisterationFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var selectedGender: String? = null
    var signedInUser: User? = null
    lateinit var phone: TextInputEditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(RegistrationViewModel::class.java)
        _binding = RegisterationFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        showTapTarget()

        val userName = binding.username

        phone = binding.phoneNumber
        val password = binding.password

        binding.submitAccountBtn.setOnClickListener {
            if (userNameIsValid(userName.text.toString().trim()) and phoneIsValid(
                    phone.text.toString().trim()
                ) and passwordIsValid(password.text.toString().trim())
            ) {
                viewModel.checkIfNumberIsAlreadySignedUp(phone.text.toString().trim())
            } else if (!userNameIsValid(userName.text.toString())) {
                userName.error = "اسم المستخدم غير صالح"
            } else if (!phoneIsValid(phone.text.toString())) {
                phone.error = "رقم الهاتف ليس صالحا"
            } else if (!passwordIsValid(password.text.toString())) {
                password.error = "كلمة السر ليست صالحة"
            }
        }

        viewModel.numberStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                NumberStatus.LOADING -> {
                    binding.submitAccountBtn.visibility = View.INVISIBLE
                }
                NumberStatus.AVAILABLE -> {
                    binding.submitAccountBtn.visibility = View.VISIBLE
                    viewModel.signUp(
                        User(
                            id= Random(2000).nextLong().toString(),
                            phone = phone.text.toString().trim(),
                            gender = selectedGender!!,
                            password = password.text.toString().trim()
                        )
                    )
                }
                NumberStatus.NOT_AVAILABLE -> {
                    binding.submitAccountBtn.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "هذا الحساب مسجل لدينا بالفعل!\n جرب تسجيل الدخول",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NumberStatus.ERROR -> {
                    binding.submitAccountBtn.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "حصل خطأ أثناء تسجيل الحساب ...حاول في وقت أخر",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            signedInUser = user
        })

        viewModel.registrationStatus.observe(viewLifecycleOwner, Observer { status ->
            when (status) {
                RegistrationStatus.LOADING -> {
                    binding.submitAccountBtn.visibility = View.INVISIBLE
                }
                RegistrationStatus.DONE -> {
                    binding.submitAccountBtn.visibility = View.VISIBLE
                    StoredAuthUser.setUser(requireContext(), phone.text.toString().trim())
                    goToNextScreen()
                }
                RegistrationStatus.ERROR -> {
                    binding.submitAccountBtn.visibility = View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        "حصل خطأ أثناء تسجيل الحساب ...حاول في وقت أخر",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        binding.registerManImage.setOnClickListener {
            selectGender("man", selectedGender, it, binding.registerWomanImage)
            binding.registerFormContainer.visibility = View.VISIBLE
            binding.whoAreYou.visibility = View.GONE
        }

        binding.registerWomanImage.setOnClickListener {
            selectGender("woman", selectedGender, binding.registerManImage, it)
            binding.registerFormContainer.visibility = View.VISIBLE
            binding.whoAreYou.visibility = View.GONE
        }

        return root
    }


    private fun goToNextScreen() {
        val bundle = bundleOf("gender" to selectedGender, "phone" to phone.text.toString().trim())
        this.findNavController()
            .navigate(R.id.action_registerationFragment_to_questionsFragment,
                bundle,
                navOptions {
                    anim {
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
    }

    private fun userNameIsValid(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun passwordIsValid(password: String): Boolean {
        return password.isNotEmpty()
    }

    private fun phoneIsValid(phone: String): Boolean {
        return phone.isNotEmpty()
    }

    private fun showTapTarget() {
        TapTargetView.showFor(requireActivity(),  // `this` is an Activity
            TapTarget.forView(
                binding.registerManImage,
                "هذه أول خطوة للبحث عن شريك الحياة",
                "أخبرنا بالمزيد عنك حتى نتمكن من مساعدتك"
            ) // All options below are optional
                .outerCircleColor(R.color.primary) // Specify a color for the outer circle
                .outerCircleAlpha(0.96f) // Specify the alpha amount for the outer circle
                .targetCircleColor(R.color.transparent) // Specify a color for the target circle
                .titleTextSize(20) // Specify the size (in sp) of the title text
                .titleTextColor(R.color.mdtp_background_color) // Specify the color of the title text
                .descriptionTextSize(15) // Specify the size (in sp) of the description text
                .descriptionTextColor(R.color.mdtp_background_color) // Specify the color of the description text
                .textColor(R.color.color_state_white_1) // Specify a color for both the title and description text
                .textTypeface(Typeface.SANS_SERIF) // Specify a typeface for the text
                .dimColor(R.color.grey_100) // If set, will dim behind the view with 30% opacity of the given color
                .drawShadow(true) // Whether to draw a drop shadow or not
                .cancelable(false) // Whether tapping outside the outer circle dismisses the view
                .tintTarget(true) // Whether to tint the target view's color
                .cancelable(true)
                .transparentTarget(false) // Specify whether the target is transparent (displays the content underneath)
//                .icon(resources.getDrawable(R.drawable.man)) // Specify a custom drawable to draw as the target
                .targetRadius(90),  // Specify the target radius (in dp)
            object : TapTargetView.Listener() {
                // The listener can listen for regular clicks, long clicks or cancels
                override fun onTargetClick(view: TapTargetView) {
                    super.onTargetClick(view) // This call is optional
                }
            })
    }

    fun selectGender(
        gender: String,
        currentSelectedGender: String?,
        manView: View,
        womanView: View
    ) {
        if (currentSelectedGender == null) {
            if (gender == "man") {
                selectedGender = "man"
                manView.background = resources.getDrawable(R.drawable.man_or_woman_images_border)
                womanView.setPadding(35, 35, 35, 35)
            } else {
                selectedGender = "woman"
                womanView.background = resources.getDrawable(R.drawable.man_or_woman_images_border)
                manView.setPadding(35, 35, 35, 35)
            }
        } else {
            if (gender == "man") {
                selectedGender = "man"
                manView.background = resources.getDrawable(R.drawable.man_or_woman_images_border)
                womanView.background = null
                womanView.setPadding(35, 35, 35, 35)
                manView.setPadding(0, 0, 0, 0)
            } else {
                selectedGender = "woman"
                womanView.background = resources.getDrawable(R.drawable.man_or_woman_images_border)
                manView.background = null
                manView.setPadding(35, 35, 35, 35)
                womanView.setPadding(0, 0, 0, 0)
            }
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        // TODO: Use the ViewModel
    }


}