package com.muslims.firebasemvvm.services

import android.util.Log
import com.muslims.firebasemvvm.models.UsersList
import com.muslims.firebasemvvm.ui.splash.GettingUsersStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object UsersApiService {


    val api: UsersApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://letaskono-api.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsersApi::class.java)
    }

    fun getAllUsers(): UsersList? {
        var usersList: UsersList? = null


        return usersList
    }
}