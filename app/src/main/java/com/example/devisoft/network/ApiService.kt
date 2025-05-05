package com.example.devisoft.network

import com.example.devisoft.models.OutstandingItem
import com.example.devisoft.models.Company
import com.example.devisoft.models.FinancialYear
import com.example.devisoft.models.LoginResponse
import com.example.devisoft.models.PurchaseResponse
import com.example.devisoft.models.SummaryResponseItem
import com.example.devisoft.models.TotalSalesResponse
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
    @POST("/api/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>


    @GET("/api/financial-years")
    suspend fun getFinancialYears(
        @Header("Authorization") token: String,
        @Header("userId") userId: String
    ): Response<List<FinancialYear>>


    // Add headers for both Authorization and User-ID
    @GET("/api/company-details")
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


    @GET("/api/get-summary")
    suspend fun getSummary(
        @Query("CompCode") compCode: String,
        @Header("UserId") userId: String,
        @Header("accessToken") accessToken: String
    ): List<SummaryResponseItem>

    @GET("/api/purchase-summary")
    suspend fun getPurchaseSummary(
        @Query("CompCode") compCode: String,
        @Header("UserId") userId: String,
        @Header("accessToken") accessToken: String
    ): List<SummaryResponseItem>

    @GET("/app/total-salesummary")
    suspend fun getTotalSalesWithHeaders(
        @Header("Authorization") accessToken: String,
        @Query("CompCode") compCode: String,
        @Query("dateFilter") dateFilter: String,
        @Header("userId") userId: String
    ): Response<TotalSalesResponse>

    @GET("/app/total-purchasesummary")
    suspend fun getTotalPurchaseWithHeaders(
        @Query("CompCode") compCode: String,
        @Query("dateFilter") dateFilter: String,
        @Header("Authorization") accessToken: String,
        @Header("userId") userId: String
    ): Response<PurchaseResponse>

}



