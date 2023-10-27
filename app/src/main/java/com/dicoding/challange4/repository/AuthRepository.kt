package com.dicoding.challange4.repository

import com.dicoding.challange4.data.RegisterBody
import com.dicoding.challange4.data.ValidateEmailBody
import com.dicoding.challange4.utils.APIConsumer
import com.dicoding.challange4.utils.RequestStatus
import com.dicoding.challange4.utils.SimplifiedMessage
import kotlinx.coroutines.flow.flow


class AuthRepository(private val consumer: APIConsumer) {
    fun validateEmailAddress(body: ValidateEmailBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateEmailAddress(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
        }
    }

    fun registerUser(body: RegisterBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.registerUser(body)
        if (response.isSuccessful) {
            emit(RequestStatus.Success(response.body()!!))
        } else {
            emit(RequestStatus.Error(SimplifiedMessage.get(response.errorBody()!!.byteStream().reader().readText())))
        }
    }
}