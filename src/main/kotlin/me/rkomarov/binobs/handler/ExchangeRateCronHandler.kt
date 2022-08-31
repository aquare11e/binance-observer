package me.rkomarov.binobs.handler

import me.rkomarov.binobs.client.ExchangeRateClient
import me.rkomarov.binobs.client.InfluxdbClient
import me.rkomarov.binobs.config.currencies.ExchangeCurrencyConfiguration
import me.rkomarov.binobs.config.environment.EnvConfiguration
import me.rkomarov.binobs.config.environment.EnvConfigurationParameter.CURRENCY_REQUESTS_CRON_EXPRESSION
import me.rkomarov.binobs.model.ExchangeRateMeasurement

class ExchangeRateCronHandler(
    override val configuration: ExchangeCurrencyConfiguration,
    private val exchangeRateClient: ExchangeRateClient,
    private val influxdbClient: InfluxdbClient
): CronHandler(configuration, EnvConfiguration[CURRENCY_REQUESTS_CRON_EXPRESSION], true) {
    override suspend fun handle() {
        val rate = exchangeRateClient.getRate(configuration.currencyOne, configuration.currencyTwo)
        influxdbClient.sendMeasurement(ExchangeRateMeasurement(rate), configuration.bucketName)
    }
}