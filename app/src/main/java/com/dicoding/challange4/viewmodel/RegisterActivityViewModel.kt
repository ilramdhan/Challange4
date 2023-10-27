package com.dicoding.challange4.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.challange4.data.RegisterBody
import com.dicoding.challange4.data.User
import com.dicoding.challange4.data.ValidateEmailBody
import com.dicoding.challange4.repository.AuthRepository
import com.dicoding.challange4.utils.RequestStatus
import kotlinx.coroutines.launch


class RegisterActivityViewModel(val authRepository: AuthRepository, val application: Application) : ViewModel() {
    private val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private val errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()
    private val isUniqueEmail: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private val user: MutableLiveData<User> = MutableLiveData()

    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage
    fun getIsUniqueEmail(): LiveData<Boolean> = isUniqueEmail
    fun getUser(): LiveData<User> = user

    fun validateEmailAddress(body: ValidateEmailBody) {
        viewModelScope.launch {
            authRepository.validateEmailAddress(body).collect(){
                when (it) {
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        isUniqueEmail.value = it.data.isUnique
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                }
            }
        }
    }

    fun registerUser(body: RegisterBody){
        viewModelScope.launch {
            authRepository.registerUser(body).collect {
                when (it) {
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        user.value = it.data.user
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                }
            }
        }

    }
}