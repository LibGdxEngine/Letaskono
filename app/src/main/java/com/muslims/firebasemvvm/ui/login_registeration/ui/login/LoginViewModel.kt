package com.muslims.firebasemvvm.ui.login_registeration.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns

import com.muslims.firebasemvvm.R
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UserModel
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.ui.login_registeration.data.LoginRepository
import com.muslims.firebasemvvm.ui.login_registeration.data.Result
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<User>()
    val loginResult: LiveData<User> = _loginResult

    fun login(username: String, password: String) {
        try {
            UsersApiService.api.getUserByPhone(username).enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

                    if (response.body()!!.user != null) {
                        if(response.body()!!.user!!.password == password){
                            _loginResult.value = response.body()!!.user
                        }
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

            })
        } catch (e: Throwable) {
        }
//        // can be launched in a separate asynchronous job
//        viewModelScope.launch {
//            val result = loginRepository.login(username, password)
//
//            if (result is Result.Success && result.data.password == password) {
//                _loginResult.value = LoginResult(result.data)
//            } else {
//                _loginResult.value = LoginResult(error = R.string.login_failed)
//            }
//        }

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