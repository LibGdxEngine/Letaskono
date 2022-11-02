package com.muslims.firebasemvvm.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.main_questions_form.AuthenticationStatus
import kotlinx.coroutines.launch

enum class AuthenticationStatus { LOADING, ERROR, DONE }
class ProfileViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _getUserStatus = MutableLiveData<AuthenticationStatus>()
    val getUserStatus: LiveData<AuthenticationStatus> = _getUserStatus

    fun getSignedInUser(userId: String) {
        viewModelScope.launch {
            _getUserStatus.value = AuthenticationStatus.LOADING
            val currentUser = UsersServices.getUserById(userId)
            if (currentUser != null) {
                _user.value = currentUser
                _getUserStatus.value = AuthenticationStatus.DONE
            } else {
                _getUserStatus.value = AuthenticationStatus.ERROR
            }
        }
    }
}