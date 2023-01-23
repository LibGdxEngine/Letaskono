package com.muslims.firebasemvvm.services

import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UserModel
import com.muslims.firebasemvvm.models.UsersList
import retrofit2.Call
import retrofit2.http.*

interface UsersApi {
    @GET("api/users")
    fun getAllUsers(): Call<UsersList>

    @PUT("api/users/{id}")
    fun updateUser(@Path("id") userId: String, @Body user: User): Call<User>

    @POST("api/users/")
    fun createUser(@Body user: User): Call<User>

    @GET("api/users/{id}")
    fun getUserByPhone(@Path("id") userId: String): Call<UserModel>


}