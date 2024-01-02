package ru.transaero21.mt.models.units.staff

import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.managers.Staff

class StaffImpl(fullName: String = "StaffImpl") : Staff(fullName) {
    override val rank: Rank = staffRank
    override val defaultProcessingTime: Float = DEFAULT_PROCESSING_TIME
    override val maxEfficiencyBonus: Float = MAXIMUM_EFFICIENCY_BONUS
    override val defaultEfficiency: Float = DEFAULT_EFFICIENCY
    override val maxEfficiency: Float = MAXIMUM_EFFICIENCY
    override val efficiencyIncreaseFactor: Float = EFFICIENCY_INCREASE_FACTOR

    companion object {
        private val staffRank = Rank(title = "StaffImpl", weight = 0F)
        private const val DEFAULT_PROCESSING_TIME = 1F
        private const val MAXIMUM_EFFICIENCY_BONUS = 0.5F
        private const val DEFAULT_EFFICIENCY = 0.1F
        private const val MAXIMUM_EFFICIENCY = 1F
        private const val EFFICIENCY_INCREASE_FACTOR = 0.002F
    }
}