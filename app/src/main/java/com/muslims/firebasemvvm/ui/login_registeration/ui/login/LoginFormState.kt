package com.muslims.firebasemvvm.ui.login_registeration.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val userPhoneError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false,
)