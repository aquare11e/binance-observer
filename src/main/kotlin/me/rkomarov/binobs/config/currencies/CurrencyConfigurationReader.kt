package me.rkomarov.binobs.config.currencies

import java.io.File
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.rkomarov.binobs.config.environment.EnvConfiguration
import me.rkomarov.binobs.config.environment.EnvConfigurationParameter
import mu.KotlinLogging

fun readCurrencyPairs(
    json: Json = Json,
    path: String = EnvConfiguration[EnvConfigurationParameter.CONFIG_DIRECTORY],
    skipErrors: Boolean = EnvConfiguration[EnvConfigurationParameter.SKIP_CONFIG_ERRORS].toBoolean()
): List<CurrencyPairConfiguration> {
    val pairs = File(path)
        .walk()
        .filter { it.isFile }
        .mapNotNull { decodeJson(it.readText(), it.name, json, skipErrors) }
        .toList()

    if (pairs.isEmpty()) {
        throw IllegalArgumentException("There is no currency pairs from config files")
    }
    return pairs
}

private fun decodeJson(text: String, filename: String, json: Json, skipErrors: Boolean): CurrencyPairConfiguration? {
    return try {
        json.decodeFromString<CurrencyPairConfiguration>(text).also {
            logger.debug { "Successfully decoded configuration file: $filename" }
        }
    } catch (e: Exception) {
        logger.warn { "Exception happened (${e.javaClass}) on decoding configuration JSON: $filename" }
        if (skipErrors) {
            logger.warn { "Skipped $filename with exception: [${e.message}]" }
            null
        } else {
            throw e
        }
    }
}

val logger = KotlinLogging.logger {}
