package me.rkomarov.binobs.handler

import java.time.Instant
import me.rkomarov.binobs.client.BinanceClient
import me.rkomarov.binobs.client.InfluxdbClient
import me.rkomarov.binobs.config.currencies.SingleCurrencyConfiguration
import me.rkomarov.binobs.model.OfferRequest
import me.rkomarov.binobs.model.toPriceOfferMeasurement
import mu.KotlinLogging

class CurrencyOfferCronHandler(
    override val configuration: SingleCurrencyConfiguration,
    private val binanceClient: BinanceClient,
    private val influxdbClient: InfluxdbClient
): CronHandler(configuration, configuration.cronExpression, false) {

    private val logger = KotlinLogging.logger {}

    override suspend fun handle() {
        logger.debug { "Send new request (${configuration.selfDescribe()})" }
        try {
            val offerRequest = configuration.toOfferRequest()
            val binanceOffers = binanceClient.getOffers(offerRequest)

            val currentTime = Instant.now()
            val measurements = binanceOffers.data
                // TODO Generic filters
                .filter { it.advertiser.monthOrderCount > 100 }
                .map { it.toPriceOfferMeasurement(currentTime) }

            influxdbClient.sendMeasurements(measurements, configuration.bucketName)
        } catch (e: Exception) {
            logger.error(e) { "Exception happens" }
        }
    }
}

private fun SingleCurrencyConfiguration.toOfferRequest(): OfferRequest {
    return OfferRequest(
        rows = rowsPerRequest,
        payTypes = paymentTypes,
        countries = countries,
        asset = asset,
        fiat = fiat,
        transAmount = amount,
        tradeType = tradeType.name
    )
}