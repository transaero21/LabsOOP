package ru.transaero21.mt.models.units

/**
 * Abstract class representing a combat unit.
 */
abstract class CombatUnit {
    /**
     * The full name of the combat unit.
     */
    abstract val fullName: String

    /**
     * The rank of the combat unit.
     */
    abstract val rank: Rank


    /**
     * The uniform worn by the combat unit.
     */
    abstract val uniform: Uniform
}
