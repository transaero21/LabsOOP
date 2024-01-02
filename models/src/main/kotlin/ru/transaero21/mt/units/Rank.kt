package ru.transaero21.mt.units

/**
 * Represents the rank of a combat unit.
 *
 * @property title the title of the rank, e.g., "Private", "Sergeant", etc.
 * @property weight the weight associated with the rank.
 */
data class Rank(
    val title: String,
    val weight: Float
)
