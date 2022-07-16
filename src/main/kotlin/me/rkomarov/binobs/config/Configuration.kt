package me.rkomarov.binobs.config

object Configuration {
    private val configMap: Map<ConfigurationParameter, String> = ConfigurationParameter.values()
        .map { it to getValueOrDefault(it) }
        .checkValuesAndReturnNonNullable()
        .toMap()

    operator fun get(configurationParameter: ConfigurationParameter) = configMap[configurationParameter]!!
}

private fun getValueOrDefault(configurationParameter: ConfigurationParameter): String? {
    val value = System.getenv(configurationParameter.name)
    return if (value.isNullOrBlank()) {
        configurationParameter.default
    } else {
        value
    }
}

private fun List<Pair<ConfigurationParameter, String?>>.checkValuesAndReturnNonNullable(): List<Pair<ConfigurationParameter, String>> {
    val invalidPairs = this.filter { it.second == null }
    if (invalidPairs.isNotEmpty()) {
        throw IllegalArgumentException(
            "Value(-s) is/are not set for the following: " + invalidPairs.joinToString(separator = ", ") { it.first.name }
        )
    }

    return this.map { it.first to it.second!! }
}