package me.rkomarov.binobs

import dev.inmo.krontab.doInfinity
import dev.inmo.krontab.doOnce
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.rkomarov.binobs.client.BinanceClient
import me.rkomarov.binobs.client.InfluxdbClient
import me.rkomarov.binobs.config.Configuration
import me.rkomarov.binobs.config.ConfigurationParameter.LIVENESS_PROBES_FILE
import me.rkomarov.binobs.config.ConfigurationParameter.PROBES_CRON_EXPRESSION
import me.rkomarov.binobs.config.ConfigurationParameter.PROPOSAL_REQUESTS_CRON_EXPRESSION
import me.rkomarov.binobs.config.ConfigurationParameter.READINESS_PROBE_FILE
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

suspend fun main(): Unit = coroutineScope {
    val binanceClient = BinanceClient(
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    )
    val influxdbClient = InfluxdbClient()

    logger.info { "Binance observer started" }

    launch {
        startProposalsRequests(binanceClient, influxdbClient)
    }

    launch {
        startProbes()
    }
}

suspend fun startProposalsRequests(
    binanceClient: BinanceClient,
    influxdbClient: InfluxdbClient
) = coroutineScope {
    doInfinity(Configuration[PROPOSAL_REQUESTS_CRON_EXPRESSION]) {
        logger.debug { "Send new request" }
        try {
            val binanceProposals = binanceClient.getProposals()
            influxdbClient.sendProposals(binanceProposals)
        } catch (e: Exception) {
            logger.error(e) { "Exception happens" }
        }
    }
}

suspend fun startProbes() {
    doOnce(Configuration[PROBES_CRON_EXPRESSION]) {
        withContext(Dispatchers.IO) {
            File(Configuration[READINESS_PROBE_FILE]).createNewFile()
            logger.debug { "Readiness probe file created" }
        }
    }

    doInfinity(Configuration[PROBES_CRON_EXPRESSION]) {
        withContext(Dispatchers.IO) {
            File(Configuration[LIVENESS_PROBES_FILE]).createNewFile()
        }
    }
}
