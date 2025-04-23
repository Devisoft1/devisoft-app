package com.example.devisoft.network

import com.example.devisoft.models.OutstandingItem
import com.example.devisoft.models.Company
import com.example.devisoft.models.FinancialYear
import com.example.devisoft.models.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


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


    // Add headers for both Authorization and User-ID
    @GET("company-details")
    suspend fun getCompanyDetails(
        @Header("Authorization") token: String,        // Access Token
        @Header("userId") userId: String,            // User ID
        @Query("fromDate") fromDate: String,          // From date
        @Query("toDate") toDate: String              // To date
    ): Response<List<Company>>

    @GET("/api/outstanding-receivables")
    suspend fun getOutstandingReceivables(
        @Header("Authorization") token: String,
        @Header("UserId") userId: String,
        @Query("type") type: String = "Receivable",
        @Query("dueDays") dueDays: Int = 0,
        @Query("criteria") criteria: String = "datewise",
        @Query("operator") operator: String = "",
        @Query("date") date: String,
        @Query("CompCode") compCode: String
    ): Response<List<OutstandingItem>>

}


