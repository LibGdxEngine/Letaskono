package com.muslims.firebasemvvm.ui.login_registeration.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import kotlinx.coroutines.launch
import java.lang.Exception

enum class RegistrationStatus { LOADING, ERROR, DONE }
enum class NumberStatus { LOADING, ERROR, AVAILABLE, NOT_AVAILABLE }
class RegistrationViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _numberStatus = MutableLiveData<NumberStatus>()
    val numberStatus: LiveData<NumberStatus> = _numberStatus

    private val _registrationStatus = MutableLiveData<RegistrationStatus>()
    val registrationStatus: LiveData<RegistrationStatus> = _registrationStatus

    fun checkIfNumberIsAlreadySignedUp(phone: String) {
        viewModelScope.launch {
            _numberStatus.value = NumberStatus.LOADING
            try {
                val currentUser = UsersServices.getUserByPhoneNumber(phone)
                if (currentUser != null) {
                    _numberStatus.value = NumberStatus.NOT_AVAILABLE
                } else {
                    _numberStatus.value = NumberStatus.AVAILABLE
                }
            } catch (e: Exception) {
                _numberStatus.value = NumberStatus.ERROR
            }
        }
    }

    fun signUp(user: User) {
        viewModelScope.launch {
            _registrationStatus.value = RegistrationStatus.LOADING
            val userAdded = UsersServices.addUser(user)
            if (userAdded) {
                _registrationStatus.value = RegistrationStatus.DONE
            } else {
                _registrationStatus.value = RegistrationStatus.ERROR
            }
        }
    }
}