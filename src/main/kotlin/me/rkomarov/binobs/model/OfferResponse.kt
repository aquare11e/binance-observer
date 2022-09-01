package me.rkomarov.binobs.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferResponse(
    val data: List<Offer>
)

@Serializable
data class Offer(
    val adv: Advert,
    val advertiser: Advertiser
)

@Serializable
data class Advert(
    val asset: String,
    val fiatUnit: String,
    val price: Double,
    val surplusAmount: Double
)

@Serializable
data class Advertiser(
    val nickName: String,
    val monthOrderCount: Int
)

//enum class Asset { USDT }
//enum class FiatUnit { USD }

