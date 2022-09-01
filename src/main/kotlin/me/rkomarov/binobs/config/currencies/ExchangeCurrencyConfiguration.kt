package me.rkomarov.binobs.config.currencies

data class ExchangeCurrencyConfiguration(
    val bucketName: String,
    val currencyOne: String,
    val currencyTwo: String
): HandlerConfiguration {
    override fun selfDescribe() = "$bucketName, $currencyOne, $currencyTwo"
}

fun CurrencyPairConfiguration.toExchange(): ExchangeCurrencyConfiguration? {
    return if (fiatTwo == null) {
        null
    } else {
        ExchangeCurrencyConfiguration(bucketName, fiatOne.fiat, fiatTwo.fiat)
    }
}