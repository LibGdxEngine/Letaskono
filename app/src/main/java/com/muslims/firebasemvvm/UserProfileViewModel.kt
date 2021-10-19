package com.muslims.firebasemvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserProfileViewModel : ViewModel() {
    private val _userProfile = MutableLiveData<Useree>()
    val userProfile: LiveData<Useree> = _userProfile
    private val _posts = MutableLiveData<Flow<List<Useree>>>()
    val posts: LiveData<Flow<List<Useree>>> = _posts

    init {
        viewModelScope.launch {
            FirebaseProfileService.addUser("user21")
//            _posts.value = FirebaseProfileService.getPosts("postId")
        }
        print("view mode values " + _userProfile.value)
    }

    suspend fun addUser(){
        viewModelScope.launch {
            FirebaseProfileService.addUser("user1")
        }
    }

}