package com.muslims.firebasemvvm.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UserModel
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.main_questions_form.AuthenticationStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class AuthenticationStatus { LOADING, ERROR, DONE }
class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

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
                Log.d("ProfileViewModel", t.message.toString() + call.toString())
                _getUserStatus.value = AuthenticationStatus.ERROR
            }

        })
    }

//    fun getSignedInUser(userId: String) {
//        viewModelScope.launch {
//            _getUserStatus.value = AuthenticationStatus.LOADING
//            val currentUser = UsersServices.getUserById(userId)
//            if (currentUser != null) {
//                _user.value = currentUser
//                _getUserStatus.value = AuthenticationStatus.DONE
//            } else {
//                _getUserStatus.value = AuthenticationStatus.ERROR
//            }
//        }
//    }
}