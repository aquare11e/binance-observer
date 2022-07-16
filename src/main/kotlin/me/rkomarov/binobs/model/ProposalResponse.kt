package me.rkomarov.binobs.model

import kotlinx.serialization.Serializable

@Serializable
data class ProposalResponse(
    val data: List<Proposal>
)

@Serializable
data class Proposal(
    val adv: Advert,
    val advertiser: Advertiser
)

@Serializable
data class Advert(
    val asset: Asset,
    val fiatUnit: FiatUnit,
    val price: Double,
    val surplusAmount: Double
)

@Serializable
data class Advertiser(
    val nickName: String,
    val monthOrderCount: Int
)

enum class Asset { USDT }
enum class FiatUnit { USD }

