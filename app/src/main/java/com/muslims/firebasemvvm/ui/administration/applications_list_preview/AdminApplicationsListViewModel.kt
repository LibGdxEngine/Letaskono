package com.muslims.firebasemvvm.ui.administration.applications_list_preview

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UsersList
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class FireStoreStatus { LOADING, ERROR, DONE }
class AdminApplicationsListViewModel : ViewModel() {
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
        _status.value = FireStoreStatus.LOADING
        UsersApiService.api.getAllUsers().enqueue(object : Callback<UsersList> {
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if (response.body() != null) {
                    _users.value = response.body()!!.users
                    _status.value = FireStoreStatus.DONE
                } else {
                    _status.value = FireStoreStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {
                _status.value = FireStoreStatus.ERROR
            }
        })
    }
}