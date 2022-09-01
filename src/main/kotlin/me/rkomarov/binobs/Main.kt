package me.rkomarov.binobs

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import me.rkomarov.binobs.client.BinanceClient
import me.rkomarov.binobs.client.ExchangeRateClient
import me.rkomarov.binobs.client.InfluxdbClient
import me.rkomarov.binobs.config.currencies.readCurrencyPairs
import me.rkomarov.binobs.handler.ProbesCronHandler
import me.rkomarov.binobs.handler.createHandlers
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalSerializationApi::class)
suspend fun main(): Unit = coroutineScope {
    val jsonMapper = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        explicitNulls = false
    }

    val currencyPairs = readCurrencyPairs(jsonMapper)

    val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonMapper)
        }
    }

    val binanceClient = BinanceClient(httpClient)
    val exchangeRateClient = ExchangeRateClient(httpClient)
    val influxdbClient = InfluxdbClient()

    val handlers = currencyPairs
        .flatMap { createHandlers(it, binanceClient, exchangeRateClient, influxdbClient) }
        .toList()

    supervisorScope {
        (handlers + ProbesCronHandler()).forEach {
            launch { it.start() }
        }
    }

    logger.info { "Binance observer started" }
}
