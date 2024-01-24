package ru.transaero21.mt.models.core.orders

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    Created, Processing, Processed, Assigned
}
