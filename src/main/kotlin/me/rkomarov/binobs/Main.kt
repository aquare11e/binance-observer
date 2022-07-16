package me.rkomarov.binobs

import dev.inmo.krontab.doInfinity
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import me.rkomarov.binobs.client.BinanceClient
import me.rkomarov.binobs.client.InfluxdbClient
import me.rkomarov.binobs.config.Configuration
import me.rkomarov.binobs.config.ConfigurationParameter.CRON_EXPRESSION
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

private val binanceClient = BinanceClient(
    Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
)

private val influxdbClient = InfluxdbClient()

suspend fun main() = coroutineScope {
    logger.info { "Binance observer started" }
    doInfinity(Configuration[CRON_EXPRESSION]) {
        logger.debug { "Send new request" }
        val binanceProposals = binanceClient.getProposals()
        influxdbClient.sendProposals(binanceProposals)
    }
}
