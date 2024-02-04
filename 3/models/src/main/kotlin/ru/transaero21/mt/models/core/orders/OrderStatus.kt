package ru.transaero21.mt.models.core.orders

import kotlinx.serialization.Serializable

/**
 * An enum class representing the status of an order.
 */
@Serializable
enum class OrderStatus {
    Created, Processing, Processed, Assigned
}
