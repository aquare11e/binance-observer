package me.rkomarov.binobs.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferRequest(
    val page: Int = 1,
    val rows: Int,
    val payTypes: List<String>,
    val countries: List<String>,
    val publisherType: String = "merchant",
    val transAmount: Double,
    val asset: String,
    val fiat: String,
    val tradeType: String
)