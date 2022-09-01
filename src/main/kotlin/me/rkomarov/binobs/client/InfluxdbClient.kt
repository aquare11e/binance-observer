package me.rkomarov.binobs.client

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import me.rkomarov.binobs.config.environment.EnvConfiguration
import me.rkomarov.binobs.config.environment.EnvConfigurationParameter
import mu.KotlinLogging

class InfluxdbClient(
    influxdbUrl: String = EnvConfiguration[EnvConfigurationParameter.INFLUXDB_URL],
    influxdbToken: CharArray = EnvConfiguration[EnvConfigurationParameter.INFLUXDB_TOKEN].toCharArray(),
    influxdbOrg: String = EnvConfiguration[EnvConfigurationParameter.INFLUXDB_ORG]
) {
    private val logger = KotlinLogging.logger {}

    private val client = InfluxDBClientKotlinFactory.create(
        influxdbUrl,
        influxdbToken,
        influxdbOrg
    ).getWriteKotlinApi()

    suspend fun <M> sendMeasurements(measurements: List<M>, bucket: String) {
        if (measurements.isEmpty()) {
            return
        }

        logger.debug { "Try to write data to database (bucket: $bucket)" }
        client.writeMeasurements(measurements, WritePrecision.NS, bucket)
    }

    suspend fun <M> sendMeasurement(measurement: M, bucket: String) {
        logger.debug { "Try to write data to database (bucket: $bucket)" }
        client.writeMeasurement(measurement, WritePrecision.NS, bucket)
    }
}