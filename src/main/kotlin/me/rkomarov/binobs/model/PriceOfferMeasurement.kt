package me.rkomarov.binobs.model

import com.influxdb.annotations.Column
import com.influxdb.annotations.Measurement
import java.time.Instant

@Measurement(name = "price_offer")
data class PriceOfferMeasurement(
    @Column(name = "price")
    val price: Double,

    @Column(name = "amount")
    val amount: Double,

    @Column(name = "seller", tag = true)
    val seller: String,

    @Column(timestamp = true)
    val time: Instant
)

fun Offer.toPriceOfferMeasurement(time: Instant = Instant.now()) = PriceOfferMeasurement(
    adv.price,
    adv.surplusAmount,
    advertiser.nickName,
    time
)
