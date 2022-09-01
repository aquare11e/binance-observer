package me.rkomarov.binobs.config.environment

import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

object EnvConfiguration {
    private val configMap: Map<EnvConfigurationParameter, String> = EnvConfigurationParameter.values()
        .map { it to getValueOrDefault(it) }
        .checkValuesAndReturnNonNullable()
        .toMap()

    operator fun get(configurationParameter: EnvConfigurationParameter) = configMap[configurationParameter]!!
}

private fun getValueOrDefault(configurationParameter: EnvConfigurationParameter): String? {
    val value = System.getenv(configurationParameter.name)

    fun hideSecret(value: String?, isSecret: Boolean) = if (isSecret) "*****" else value
    return if (value.isNullOrBlank()) {
        val loggedValue = hideSecret(configurationParameter.default, configurationParameter.isSecret)
        logger.debug { "${configurationParameter.name} wasn't set. Use default: $loggedValue" }
        configurationParameter.default
    } else {
        val loggedValue = hideSecret(value, configurationParameter.isSecret)
        logger.debug { "${configurationParameter.name} set: $loggedValue" }
        value
    }
}

private fun List<Pair<EnvConfigurationParameter, String?>>.checkValuesAndReturnNonNullable(): List<Pair<EnvConfigurationParameter, String>> {
    val invalidPairs = this.filter { it.second == null }
    if (invalidPairs.isNotEmpty()) {
        throw IllegalArgumentException(
            "Value(-s) is/are not set for the following: " + invalidPairs.joinToString(separator = ", ") { it.first.name }
        )
    }

    return this.map { it.first to it.second!! }
}