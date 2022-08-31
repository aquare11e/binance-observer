package me.rkomarov.binobs.config.environment

enum class EnvConfigurationParameter(val default: String?, val isSecret: Boolean) {
    OFFERS_REQUESTS_CRON_EXPRESSION("*/30 * * * *", false),
    CURRENCY_REQUESTS_CRON_EXPRESSION("* * */12 * *", false),
    PROBES_CRON_EXPRESSION("*/10 * * * *", false),

    LIVENESS_PROBES_FILE("/tmp/live", false),

    CONFIG_DIRECTORY("/config", false),
    SKIP_CONFIG_ERRORS("true", false),

    INFLUXDB_URL("http://influxdb:8086", false),
    INFLUXDB_TOKEN(null, true),
    INFLUXDB_ORG("binobs", false),
}