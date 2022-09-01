package me.rkomarov.binobs.config.currencies

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyPairConfiguration(
    val bucketName: String,
    val fiatOne: FiatConfiguration,
    val fiatTwo: FiatConfiguration?,
    val asset: String,
    val tradeType: TradeType,
    val paymentTypes: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val rowsPerRequest: Int = 20
)

@Serializable
data class FiatConfiguration(
    val fiat: String,
    val amount: Double,
    val cronExpression: String?
)

enum class TradeType {
    BUY, SELL
}