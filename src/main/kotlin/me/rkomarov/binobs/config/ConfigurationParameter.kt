package me.rkomarov.binobs.config

enum class ConfigurationParameter(val default: String?) {
    PROPOSAL_REQUESTS_CRON_EXPRESSION("/30 * * * *"),
    PROBES_CRON_EXPRESSION("/10 * * * *"),

    LIVENESS_PROBES_FILE("/tmp/live"),
    READINESS_PROBE_FILE("/tmp/ready"),

    INFLUXDB_URL("http://localhost:8086"),
    INFLUXDB_TOKEN(null),
    INFLUXDB_ORG("binobs"),
    INFLUXDB_BUCKET("binobs"),
}