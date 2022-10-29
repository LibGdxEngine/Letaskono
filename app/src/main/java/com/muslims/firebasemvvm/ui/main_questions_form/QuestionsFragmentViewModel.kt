package com.muslims.firebasemvvm.ui.main_questions_form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import kotlinx.coroutines.launch

enum class AuthenticationStatus { LOADING, ERROR, DONE }
enum class UpdatingStatus { LOADING, ERROR, DONE }
class QuestionsFragmentViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _updatingStatus = MutableLiveData<UpdatingStatus>()
    val updatingStatus: LiveData<UpdatingStatus> = _updatingStatus

    private val _getUserStatus = MutableLiveData<AuthenticationStatus>()
    val getUserStatus: LiveData<AuthenticationStatus> = _getUserStatus

    fun updateUser(user: User) {
        viewModelScope.launch {
            _updatingStatus.value = UpdatingStatus.LOADING
            try {
                UsersServices.updateUser(user)
            } catch (e: Exception) {
                _updatingStatus.value = UpdatingStatus.ERROR
            }
            _updatingStatus.value = UpdatingStatus.DONE
        }
    }

    fun getSignedInUser(phone: String) {
        viewModelScope.launch {
            _getUserStatus.value = AuthenticationStatus.LOADING
            val currentUser = UsersServices.getUserByPhoneNumber(phone)
            if (currentUser != null) {
                _user.value = currentUser
                _getUserStatus.value = AuthenticationStatus.DONE
            } else {
                _getUserStatus.value = AuthenticationStatus.ERROR
            }
        }
    }
}