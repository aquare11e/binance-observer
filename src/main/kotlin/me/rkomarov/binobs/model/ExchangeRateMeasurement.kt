package me.rkomarov.binobs.model

import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement
import java.time.Instant

@Measurement(name = "exchange_rate")
data class ExchangeRateMeasurement(
    @Column(name = "rate")
    val rate: Double,

    @Column(timestamp = true)
    val time: Instant = Instant.now()
)