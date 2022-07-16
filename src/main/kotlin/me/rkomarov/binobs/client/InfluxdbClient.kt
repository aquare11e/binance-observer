package me.rkomarov.binobs.client

import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.kotlin.InfluxDBClientKotlinFactory
import java.time.Instant
import me.rkomarov.binobs.config.Configuration
import me.rkomarov.binobs.config.ConfigurationParameter
import me.rkomarov.binobs.model.ProposalResponse
import me.rkomarov.binobs.model.toPriceMeasurement
import mu.KotlinLogging

class InfluxdbClient(
    influxdbUrl: String = Configuration[ConfigurationParameter.INFLUXDB_URL],
    influxdbToken: CharArray = Configuration[ConfigurationParameter.INFLUXDB_TOKEN].toCharArray(),
    influxdbOrg: String = Configuration[ConfigurationParameter.INFLUXDB_ORG],
    influxdbBucket: String = Configuration[ConfigurationParameter.INFLUXDB_BUCKET]
) {

    private val client = InfluxDBClientKotlinFactory.create(
        influxdbUrl,
        influxdbToken,
        influxdbOrg,
        influxdbBucket
    ).getWriteKotlinApi()

    private val logger = KotlinLogging.logger {}

    suspend fun sendProposals(proposal: ProposalResponse) {
        logger.debug { "Try to write data to database" }

        val currentTime = Instant.now()
        val proposalPriceMeasurements = proposal.data
            .filter { it.advertiser.monthOrderCount > 100 }
            .map { it.toPriceMeasurement(currentTime) }

        client.writeMeasurements(proposalPriceMeasurements, WritePrecision.NS)
    }
}