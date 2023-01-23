package com.muslims.firebasemvvm.ui.login_registeration.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UserModel
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.splash.GettingUsersStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

enum class RegistrationStatus { LOADING, ERROR, DONE }
enum class NumberStatus { LOADING, ERROR, AVAILABLE, NOT_AVAILABLE }
class RegistrationViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userModel = MutableLiveData<UserModel>()
    val userModel: LiveData<UserModel> = _userModel

    private val _numberStatus = MutableLiveData<NumberStatus>()
    val numberStatus: LiveData<NumberStatus> = _numberStatus

    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus> = _registrationStatus

    fun checkIfNumberIsAlreadySignedUp(phone: String) {
        _numberStatus.value = NumberStatus.LOADING
        UsersApiService.api.getUserByPhone(phone).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.body()!!.user != null) {
                    _userModel.value = response.body()!!
                    Log.d("Response", response.isSuccessful().toString())
                    _numberStatus.value = NumberStatus.NOT_AVAILABLE
                } else {
                    _numberStatus.value = NumberStatus.AVAILABLE
                    return
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _numberStatus.value = NumberStatus.ERROR
            }

        })
    }

    fun signUp(user: User) {
        _registrationStatus.value = RegistrationStatus.LOADING
        UsersApiService.api.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() != null) {
                    _user.value = response.body()!!
                    _registrationStatus.value = RegistrationStatus.DONE
                } else {
                    _registrationStatus.value = RegistrationStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _registrationStatus.value = RegistrationStatus.ERROR
            }

        })
    }

}