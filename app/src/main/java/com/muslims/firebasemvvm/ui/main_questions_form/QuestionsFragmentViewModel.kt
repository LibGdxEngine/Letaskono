package com.muslims.firebasemvvm.ui.main_questions_form

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

enum class AuthenticationStatus { LOADING, ERROR, DONE }
enum class UpdatingStatus { LOADING, ERROR, DONE }
class QuestionsFragmentViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _updatingStatus = MutableLiveData<UpdatingStatus>()
    val updatingStatus: LiveData<UpdatingStatus> = _updatingStatus

    private val _getUserStatus = MutableLiveData<AuthenticationStatus>()
    val getUserStatus: LiveData<AuthenticationStatus> = _getUserStatus


    fun getSignedInUser(phone: String) {
        _getUserStatus.value = AuthenticationStatus.LOADING
        UsersApiService.api.getUserByPhone(phone).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.body()!!.user != null) {
                    _user.value = response.body()!!.user
                    _getUserStatus.value = AuthenticationStatus.DONE
                } else {
                    _getUserStatus.value = AuthenticationStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _getUserStatus.value = AuthenticationStatus.ERROR
            }

        })
    }

    fun updateUser(phone: String, user: User) {
        _updatingStatus.value = UpdatingStatus.LOADING
        UsersApiService.api.updateUser(phone, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() != null) {
                    _user.value = response.body()!!
                    _updatingStatus.value = UpdatingStatus.DONE
                } else {
                    _updatingStatus.value = UpdatingStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _updatingStatus.value = UpdatingStatus.ERROR
            }

        })
    }

//    fun updateUser(user: User) {
//        viewModelScope.launch {
//            _updatingStatus.value = UpdatingStatus.LOADING
//            try {
//                UsersServices.updateUser(user)
//            } catch (e: Exception) {
//                println( "MyException" + e.message.toString())
//                _updatingStatus.value = UpdatingStatus.ERROR
//            }
//            _updatingStatus.value = UpdatingStatus.DONE
//        }
//    }

//    fun getSignedInUser(phone: String) {
//        viewModelScope.launch {
//            _getUserStatus.value = AuthenticationStatus.LOADING
//            val currentUser = UsersServices.getUserByPhoneNumber(phone)
//            if (currentUser != null) {
//                _user.value = currentUser
//                _getUserStatus.value = AuthenticationStatus.DONE
//            } else {
//                _getUserStatus.value = AuthenticationStatus.ERROR
//            }
//        }
//    }
}