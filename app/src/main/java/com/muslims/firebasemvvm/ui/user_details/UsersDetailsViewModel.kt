package com.muslims.firebasemvvm.ui.user_details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.splash.GettingUsersStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

enum class UpdatingStatus { LOADING, ERROR, DONE }
class UsersDetailsViewModel : ViewModel() {

    private val _status = MutableLiveData<UpdatingStatus>()
    val status: LiveData<UpdatingStatus> = _status



    private val _removingStatus = MutableLiveData<UpdatingStatus>()
    val removingStatus: LiveData<UpdatingStatus> = _removingStatus


    fun addUserToFavourites(phone: String, user: User) {
        _status.value = UpdatingStatus.LOADING
        UsersApiService.api.updateUser(phone, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() != null) {
                    _status.value = UpdatingStatus.DONE
                } else {
                    _status.value = UpdatingStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _status.value = UpdatingStatus.ERROR
            }

        })
    }

    fun removeFromFavourite(phone: String, user: User) {
        _removingStatus.value = UpdatingStatus.LOADING
        UsersApiService.api.updateUser(phone, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() != null) {
                    _removingStatus.value = UpdatingStatus.DONE
                } else {
                    _removingStatus.value = UpdatingStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _removingStatus.value = UpdatingStatus.ERROR
            }

        })
    }


    fun report(reported:User, reporter:User){
        viewModelScope.launch {
            _status.value = UpdatingStatus.LOADING
            try {
                UsersServices.report(reported, reporter)
                _status.value = UpdatingStatus.DONE
            } catch (e: Exception) {
                _status.value = UpdatingStatus.ERROR
            }
        }
    }

}