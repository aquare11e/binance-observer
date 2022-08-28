package me.rkomarov.binobs.model

import kotlinx.serialization.Serializable

@Serializable
data class ProposalRequest(
    val page: Int = 1,
    val rows: Int = 20,
    val payTypes: List<String> = listOf("TinkoffNew"),
    val countries: List<String> = listOf(),
    val publisherType: String = "merchant",
    val transAmount: Double = 500.0,
    val asset: String = "USDT",
    val fiat: String = "USD",
    val tradeType: String = "BUY"
)