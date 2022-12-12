package com.muslims.firebasemvvm.ui.login_registeration.ui.login

import com.muslims.firebasemvvm.models.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: User? = null,
    val error: Int? = null,
)