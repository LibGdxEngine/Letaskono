package com.muslims.firebasemvvm.utils

import com.muslims.firebasemvvm.models.User

interface AuthenticatedUser {
    fun getAuthUser(): User?
}