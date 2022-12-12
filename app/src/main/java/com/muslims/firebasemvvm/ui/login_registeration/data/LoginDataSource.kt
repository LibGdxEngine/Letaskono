package com.muslims.firebasemvvm.ui.login_registeration.data


import com.muslims.firebasemvvm.models.User
import com.muslims.firebasemvvm.services.UsersServices
import com.muslims.firebasemvvm.ui.login_registeration.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(phone: String, password: String): Result<User> {
        try {
            // TODO: handle loggedInUser authentication
            val user = UsersServices.getUserByPhoneNumber(phone)
            return Result.Success(user!!)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}