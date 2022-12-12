package com.muslims.firebasemvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus
import kotlinx.coroutines.launch

enum class AuthenticationStatus { LOADING, ERROR, DONE }
class HomeActivityViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _status = MutableLiveData<AuthenticationStatus>()
    val status: LiveData<AuthenticationStatus> = _status


    fun getSignedInUser(phone: String) {
        viewModelScope.launch {
            try {
                _status.value = AuthenticationStatus.LOADING
                val currentUser = UsersServices.getUserByPhoneNumber(phone)
                if (currentUser != null) {
                    _user.value = currentUser
                    _status.value = AuthenticationStatus.DONE
                } else {
                    _status.value = AuthenticationStatus.ERROR
                }
            } catch (error: Exception) {
                _status.value = AuthenticationStatus.ERROR
            }

        }
    }
}