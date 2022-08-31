package me.rkomarov.binobs.handler

import me.rkomarov.binobs.client.BinanceClient
import me.rkomarov.binobs.client.ExchangeRateClient
import me.rkomarov.binobs.client.InfluxdbClient
import me.rkomarov.binobs.config.currencies.CurrencyPairConfiguration
import me.rkomarov.binobs.config.currencies.toExchange
import me.rkomarov.binobs.config.currencies.toSingleOne
import me.rkomarov.binobs.config.currencies.toSingleTwo

fun createHandlers(
    configuration: CurrencyPairConfiguration,
    binanceClient: BinanceClient,
    exchangeRateClient: ExchangeRateClient,
    influxdbClient: InfluxdbClient
): List<CronHandler> {
    val firstCurrencyHandler =
        CurrencyOfferCronHandler(configuration.toSingleOne(), binanceClient, influxdbClient)
    val secondCurrencyHandler =
        configuration.toSingleTwo()?.let { CurrencyOfferCronHandler(it, binanceClient, influxdbClient) }
    val currencyExchangeHandler =
        configuration.toExchange()?.let { ExchangeRateCronHandler(it, exchangeRateClient, influxdbClient) }

    return listOfNotNull(firstCurrencyHandler, secondCurrencyHandler, currencyExchangeHandler)
}