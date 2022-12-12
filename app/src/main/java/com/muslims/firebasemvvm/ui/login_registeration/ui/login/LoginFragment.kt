package com.muslims.firebasemvvm.ui.login_registeration.ui.login

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.databinding.FragmentLoginBinding
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.ui.main_questions_form.Questions.QuestionsContent
import com.muslims.firebasemvvm.utils.StoredAuthUser


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val textViewLogo = binding.loginTextViewLogo

        textViewLogo.setCharacterDelay(150)
        textViewLogo.animateText(getString(R.string.app_name));

        binding.btnFromLoginToSignUp.setOnClickListener {
            this.findNavController().navigate(R.id.action_loginFragment_to_registerationFragment)
        }

        binding.forgotPassword.setOnClickListener {
            setClickToChat(it, "+201019867911", "السلام عليكم ورحمة الله \n أريد استعادة كلمة السر الخاصة بي")
        }

        return binding.root
    }

    fun setClickToChat(v: View, toNumber: String, message: String) {
        val url = "https://wa.me/$toNumber/?text=" + message
        try {
            val pm = v.context.packageManager
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            v.context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            v.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        val userPhoneEditText = binding.email
        val passwordEditText = binding.password
        val loginButton = binding.btnLogin
        val loadingProgressBar = binding.loginProgressBar

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.userPhoneError?.let {
                    userPhoneEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                    updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    userPhoneEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        userPhoneEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    userPhoneEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            loginViewModel.login(
                userPhoneEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }
    }

    private fun updateUiWithUser(model: User) {
        val welcome = getString(R.string.welcome) + " " + model.name
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
        StoredAuthUser.setUser(requireContext(), model.id)
        if (model.questionsList?.size!! < QuestionsContent.items(model.gender).size) {
            StoredAuthUser.setUserInfoCompleted(requireContext(), false)
        } else {
            StoredAuthUser.setUserInfoCompleted(requireContext(), true)
        }
        goToNextScreen()
    }

    private fun goToNextScreen() {
        this.findNavController()
            .navigate(R.id.action_loginFragment_to_navigation_home,
                null,
                navOptions {
                    anim {
                        enter = android.R.anim.slide_in_left
                        exit = android.R.anim.slide_out_right
                    }
                })
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}