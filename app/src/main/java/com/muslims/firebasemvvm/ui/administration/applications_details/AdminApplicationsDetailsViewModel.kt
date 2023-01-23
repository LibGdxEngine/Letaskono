package com.muslims.firebasemvvm.ui.administration.applications_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.user_details.UpdatingStatus
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

enum class UpdatingStatus { LOADING, ERROR, DONE }
enum class SearchStatus { LOADING, ERROR, DONE }
enum class RelateStatus { LOADING, ERROR, SAME_GENDER, ALREADY_RELATED, ALREADY_MARRIED, DONE }
class AdminApplicationsDetailsViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _relateStatus = MutableLiveData<RelateStatus>()
    val relateStatus: LiveData<RelateStatus> = _relateStatus

    private val _status = MutableLiveData<UpdatingStatus>()
    val status: LiveData<UpdatingStatus> = _status

    private val _removingStatus = MutableLiveData<UpdatingStatus>()
    val removingStatus: LiveData<UpdatingStatus> = _removingStatus

    private val _searchStatus = MutableLiveData<SearchStatus>()
    val searchStatus: LiveData<SearchStatus> = _searchStatus

    fun blockUser(phone: String, user: User) {
        _status.value = UpdatingStatus.LOADING
        user.isBlocked = !user.isBlocked
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
                _status.value = UpdatingStatus.ERROR
            }

        })
    }


    fun convertToWantToMarry(phone: String, user: User) {
        _status.value = UpdatingStatus.LOADING
        user.isBlocked = !user.isBlocked
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
                _status.value = UpdatingStatus.ERROR
            }

        })
    }


    fun convertToWantToMarry(user: User) {
        viewModelScope.launch {
            _status.value = UpdatingStatus.LOADING
            try {
                UsersServices.unrelate(user)
                _status.value = UpdatingStatus.DONE
            } catch (e: Exception) {
                _status.value = UpdatingStatus.ERROR
            }
        }
    }

    fun marry(currentUser: User, user: User) {
        viewModelScope.launch {
            _status.value = UpdatingStatus.LOADING
            try {
                val success = UsersServices.marry(currentUser, user)
                if (success) {
                    _status.value = UpdatingStatus.DONE
                } else {
                    _status.value = UpdatingStatus.ERROR
                }
            } catch (e: Exception) {
                _status.value = UpdatingStatus.ERROR
            }
        }
    }


    fun relate(currentUser: User, user: User) {
        viewModelScope.launch {
            _relateStatus.value = RelateStatus.LOADING
            try {
                val result = UsersServices.relate(currentUser, user)
                if (result == "SAME_GENDER") {
                    _relateStatus.value = RelateStatus.SAME_GENDER
                } else if (result == "ALREADY_RELATED") {
                    _relateStatus.value = RelateStatus.ALREADY_RELATED
                } else if (result == "ALREADY_MARRIED") {
                    _relateStatus.value = RelateStatus.ALREADY_MARRIED
                } else {
                    _relateStatus.value = RelateStatus.DONE
                }
            } catch (e: Exception) {
                _relateStatus.value = RelateStatus.ERROR
            }
        }
    }

    fun getUserByMobileOrCode(text: String, currentUserGender: String) {
        viewModelScope.launch {
            _searchStatus.value = SearchStatus.LOADING
            try {
                var user = UsersServices.getUserByMobile(text)
                if (user != null) {
                    _user.value = user
                    _searchStatus.value = SearchStatus.DONE
                } else {
                    user = UsersServices.getUserByCode(text, currentUserGender)
                    if (user != null) {
                        _user.value = user
                        _searchStatus.value = SearchStatus.DONE
                    } else {
                        _searchStatus.value = SearchStatus.ERROR
                    }
                }

            } catch (e: Exception) {
                _searchStatus.value = SearchStatus.ERROR
            }
        }
    }

    fun updateHistoryInfo(user: User) {
        viewModelScope.launch {
            _status.value = UpdatingStatus.LOADING
            try {
                UsersServices.updateUser(user)
                _status.value = UpdatingStatus.DONE
            } catch (e: Exception) {
                _status.value = UpdatingStatus.ERROR
            }
        }
    }

    fun addUserToFavourites(user: User, favourite: String) {
        viewModelScope.launch {
            _status.value = UpdatingStatus.LOADING
            try {
                UsersServices.addToFavourite(user, favourite)
                _status.value = UpdatingStatus.DONE
            } catch (e: Exception) {
                _status.value = UpdatingStatus.ERROR
            }
        }
    }


    fun removeFromFavourite(user: User, favourite: String) {
        viewModelScope.launch {
            _removingStatus.value = UpdatingStatus.LOADING
            try {
                UsersServices.removeFromFavourite(user, favourite)
                _removingStatus.value = UpdatingStatus.DONE
            } catch (e: Exception) {
                _removingStatus.value = UpdatingStatus.ERROR
            }
        }
    }
}