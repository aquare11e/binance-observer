package me.rkomarov.binobs.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import mu.KotlinLogging

class ExchangeRateClient(private val httpClient: HttpClient) {
    private val logger = KotlinLogging.logger {}

    suspend fun getRate(from: String, to: String): Double {
        logger.debug { "Send request for exchange rate $from/$to" }
        val response = httpClient.get("$EXCHANGE_API_URL/${from.lowercase()}/${to.lowercase()}.json") {}
        return response.body<Map<String, JsonPrimitive>>()[to.lowercase()]!!.double
    }

    companion object {
        private const val EXCHANGE_API_URL = "https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies"
    }
}