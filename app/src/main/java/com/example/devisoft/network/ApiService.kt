package com.example.devisoft.network

import com.example.devisoft.models.FinancialYear
import com.example.devisoft.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.POST



interface ApiService {

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("financial-years")
    suspend fun getFinancialYears(
        @Header("Authorization") token: String,
        @Header("userId") userId: String
    ): Response<List<FinancialYear>>



}


