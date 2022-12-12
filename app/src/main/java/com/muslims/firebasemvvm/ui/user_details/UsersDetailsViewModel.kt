package com.muslims.firebasemvvm.ui.user_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import kotlinx.coroutines.launch
import java.lang.Exception

enum class UpdatingStatus { LOADING, ERROR, DONE }
class UsersDetailsViewModel : ViewModel() {

    private val _status = MutableLiveData<UpdatingStatus>()
    val status: LiveData<UpdatingStatus> = _status

    private val _removingStatus = MutableLiveData<UpdatingStatus>()
    val removingStatus: LiveData<UpdatingStatus> = _removingStatus

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