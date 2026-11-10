package com.videshkarmakar.itube.data.model

import com.videshkarmakar.itube.data.local.entity.SubscriptionGroupEntity

data class SubscriptionGroup(
    val name: String,
    val channelIds: List<String>,
    val sortOrder: Int = 0,
)

fun SubscriptionGroupEntity.toUiModel() =
    SubscriptionGroup(
        name = name,
        channelIds = if (channelIds.isBlank()) emptyList() else channelIds.split(",").filter { it.isNotBlank() },
        sortOrder = sortOrder,
    )
