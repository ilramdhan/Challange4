package com.dicoding.challange4.utils

import com.dicoding.challange4.data.RegisterBody
import com.dicoding.challange4.data.RegisterResponse
import com.dicoding.challange4.data.UniqueEmailValidationResponse
import com.dicoding.challange4.data.ValidateEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {

    @POST("users/validate-unique-email")
    suspend fun validateEmailAddress(@Body body: ValidateEmailBody): Response<UniqueEmailValidationResponse>

    @POST("users/register")
    suspend fun registerUser(@Body body: RegisterBody): Response<RegisterResponse>

}