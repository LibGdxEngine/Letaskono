package com.muslims.firebasemvvm.ui.splash


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UserModel
import com.muslims.firebasemvvm.models.UsersList
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.users_applications_home.FireStoreStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class GettingUsersStatus { LOADING, ERROR, DONE }
class SplashViewModel : ViewModel() {
    private val TAG = "SplashViewModel"

    private val _users = MutableLiveData<UsersList>()
    val users: LiveData<UsersList> = _users

    private val _fireUsers = MutableLiveData<List<User>>()
    val fireUsers: LiveData<List<User>> = _fireUsers

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _userModel = MutableLiveData<UserModel>()
    val userModel: LiveData<UserModel> = _userModel

    private val _status = MutableLiveData<GettingUsersStatus>()
    val status: LiveData<GettingUsersStatus> = _status

    fun getUserByPhone(phone: String) {
        _status.value = GettingUsersStatus.LOADING
        UsersApiService.api.getUserByPhone(phone).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                println("World " + response)
                if (response.body() != null) {
                    _userModel.value = response.body()!!
                    println("World " + response.body()!!.user!!.phone)
                    _status.value = GettingUsersStatus.DONE
                } else {
                    _status.value = GettingUsersStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _status.value = GettingUsersStatus.ERROR
            }

        })
    }

    fun updateUser(phone: String, user: User) {
        _status.value = GettingUsersStatus.LOADING
        UsersApiService.api.updateUser(phone, user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                println("World " + response)
                if (response.body() != null) {
                    _user.value = response.body()!!
                    println("World " + response.body()!!)
                    _status.value = GettingUsersStatus.DONE
                } else {
                    _status.value = GettingUsersStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _status.value = GettingUsersStatus.ERROR
            }

        })
    }

    fun createNewUser(user: User) {
        _status.value = GettingUsersStatus.LOADING
        UsersApiService.api.createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                println("World " + response)
                if (response.body() != null) {
                    _user.value = response.body()!!
                    println("World " + response.body()!!)
                    _status.value = GettingUsersStatus.DONE
                } else {
                    _status.value = GettingUsersStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("onFailure", t.message.toString())
                _status.value = GettingUsersStatus.ERROR
            }

        })
    }

    fun getAllUsers() {
        _status.value = GettingUsersStatus.LOADING
        UsersApiService.api.getAllUsers().enqueue(object : Callback<UsersList> {
            override fun onResponse(call: Call<UsersList>, response: Response<UsersList>) {
                if (response.body() != null) {
                    Log.d(TAG, "Got response successfully")
                    _users.value = response.body()!!
                    _status.value = GettingUsersStatus.DONE
                } else {
                    Log.d(TAG, "No users found")
                    _status.value = GettingUsersStatus.ERROR
                    return
                }
            }

            override fun onFailure(call: Call<UsersList>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                _status.value = GettingUsersStatus.ERROR
            }
        })
    }

    fun getAllUsersFromFirebase() {
        viewModelScope.launch {
            _status.value = GettingUsersStatus.LOADING
            _fireUsers.value = UsersServices.getAllUsers()
            _status.value = GettingUsersStatus.DONE
        }
    }
}