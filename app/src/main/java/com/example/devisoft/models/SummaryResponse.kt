package com.example.devisoft.models

data class SummaryResponse(
    val TodayCash: String? = "0",
    val TodayCredit: String? = "0",
    val TodayCard: String? = "0",
    val TodayWallet: String? = "0",
    val TodayCreditNote: String? = "0",
    val TotalSaleToday: String? = "0"
)
