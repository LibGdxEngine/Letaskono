package com.muslims.firebasemvvm.ui.login_registeration.data


import android.util.Log
import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.models.UserModel
import com.muslims.firebasemvvm.services.UsersApiService
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.login_registeration.data.model.LoggedInUser
import com.muslims.firebasemvvm.ui.main_questions_form.AuthenticationStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(phone: String, password: String): Result<User> {
        var result: User? = null
        try {
            UsersApiService.api.getUserByPhone(phone).enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {

                    if (response.body()!!.user != null) {
                        if(response.body()!!.user!!.password == password){
                            result = response.body()!!.user
                            Log.d("Response", response.body()!!.user!!.phone)
                        }
                    } else {
                        return
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Log.d("onFailure", t.message.toString())
                }

            })
            return Result.Success(result!!)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}