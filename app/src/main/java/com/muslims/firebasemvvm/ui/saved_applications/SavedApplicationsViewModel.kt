package com.muslims.firebasemvvm.ui.saved_applications

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus
import kotlinx.coroutines.launch

class SavedApplicationsViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    private val _status = MutableLiveData<FireStoreStatus>()
    val status: LiveData<FireStoreStatus> = _status

    init {
        if (_users.value == null) {
            getAllUsers()
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            _status.value = FireStoreStatus.LOADING
            _users.value = UsersServices.getAllUsers()
            _status.value = FireStoreStatus.DONE
        }
    }
}