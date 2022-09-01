package me.rkomarov.binobs.config.currencies

data class SingleCurrencyConfiguration(
    val bucketName: String,
    val cronExpression: String?,
    val fiat: String,
    val amount: Double,
    val asset: String,
    val tradeType: TradeType,
    val paymentTypes: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val rowsPerRequest: Int = 20
): HandlerConfiguration {
    override fun selfDescribe() = "$bucketName, $fiat, $asset, $tradeType"
}

fun CurrencyPairConfiguration.toSingleOne() = SingleCurrencyConfiguration(
    bucketName, fiatOne.cronExpression, fiatOne.fiat, fiatOne.amount, asset, tradeType,
    paymentTypes, countries, rowsPerRequest
)

fun CurrencyPairConfiguration.toSingleTwo(): SingleCurrencyConfiguration? {
    return if (fiatTwo == null) {
        null
    } else {
        SingleCurrencyConfiguration(
            bucketName, fiatTwo.cronExpression, fiatTwo.fiat, fiatTwo.amount, asset, tradeType,
            paymentTypes, countries, rowsPerRequest
        )
    }
}