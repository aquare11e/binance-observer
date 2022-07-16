package me.rkomarov.binobs.config

enum class ConfigurationParameter(val default: String?) {
    CRON_EXPRESSION("/30 * * * *"),

    INFLUXDB_URL("http://localhost:8086"),
    INFLUXDB_TOKEN(null),
    INFLUXDB_ORG("binobs"),
    INFLUXDB_BUCKET("binobs"),
}