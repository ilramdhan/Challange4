package com.dicoding.challange4.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.challange4.repository.AuthRepository
import java.security.InvalidParameterException

class RegisterActivityViewModelFactory(private val authRepository: AuthRepository, private val application: Application): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)) {
            return RegisterActivityViewModel(authRepository, application) as T
        }

        throw InvalidParameterException("Unable to construct RegisterActivityViewModel")
    }
}