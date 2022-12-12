package com.muslims.firebasemvvm.ui.login_registeration.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.auth.User
import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.ui.login_registeration.data.LoginRepository
import com.muslims.firebasemvvm.ui.login_registeration.data.Result
import kotlinx.coroutines.launch


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = loginRepository.login(username, password)

            if (result is Result.Success && result.data.password == password) {
                _loginResult.value = LoginResult(result.data)
            } else {
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }

    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserPhoneValid(username)) {
            _loginForm.value = LoginFormState(userPhoneError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserPhoneValid(userPhone: String): Boolean {
        return if (isNumeric(userPhone)) {
            Patterns.PHONE.matcher(userPhone).matches()
        } else {
            userPhone.isNotBlank()
        }
    }

    fun isNumeric(toCheck: String): Boolean {
        return toCheck.all { char -> char.isDigit() }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}