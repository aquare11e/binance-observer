package me.rkomarov.binobs.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.rkomarov.binobs.model.ProposalRequest
import me.rkomarov.binobs.model.ProposalResponse
import mu.KotlinLogging

class BinanceClient(jsonMapper: Json) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(jsonMapper)
        }
    }

    private val logger = KotlinLogging.logger {}

    suspend fun getProposals(): ProposalResponse {
        val response = client.post("${Companion.BINANCE_API_URL}/bapi/c2c/v2/friendly/c2c/adv/search") {
            contentType(ContentType.Application.Json)
            setBody(ProposalRequest())
        }

        logger.debug { "Received new proposals" }
        return response.body()
    }

    companion object {
        private const val BINANCE_API_URL = "https://p2p.binance.com"
    }
}