package com.muslims.firebasemvvm.ui.users_applications_home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UsersList
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.splash.GettingUsersStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class FireStoreStatus { LOADING, ERROR, DONE }

class HomeViewModel : ViewModel() {
    private val TAG = "HomeViewModel"

    private val _users = MutableLiveData<UsersList>()
    val users: LiveData<UsersList> = _users

    private val _status = MutableLiveData<FireStoreStatus>()
    val status: LiveData<FireStoreStatus> = _status


    init {
        if (_users.value == null) {
            getAllUsers()
        }
    }

    fun getAllUsers() {
        _status.value = FireStoreStatus.LOADING
        UsersApiService.api.getAllUsers().enqueue(object : Callback<UsersList> {
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if (response.body() != null) {
                    _users.value = response.body()!!
                    _status.value = FireStoreStatus.DONE
                } else {
                    Log.d(TAG, "No users found")
                    _status.value = FireStoreStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                _status.value = FireStoreStatus.ERROR
            }
        })
    }

}