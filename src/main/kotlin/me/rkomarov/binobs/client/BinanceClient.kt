package me.rkomarov.binobs.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.rkomarov.binobs.model.OfferRequest
import me.rkomarov.binobs.model.OfferResponse
import mu.KotlinLogging

class BinanceClient(private val httpClient: HttpClient) {
    private val logger = KotlinLogging.logger {}

    suspend fun getOffers(offerRequest: OfferRequest): OfferResponse {
        val response = httpClient.post("${BINANCE_API_URL}/bapi/c2c/v2/friendly/c2c/adv/search") {
            contentType(ContentType.Application.Json)
            setBody(offerRequest)
        }

        logger.debug { "Received new offers" }
        return response.body()
    }

    companion object {
        private const val BINANCE_API_URL = "https://p2p.binance.com"
    }
}