package ru.transaero21.mt.models.units.managers

import ru.transaero21.mt.models.units.Rank

class CommonStaff(
    override val rank: Rank,
    fullName: String
) : Staff(fullName = fullName) {
    override val defaultProcessingTime: Float = 1f
    override val maxEfficiencyBonus: Float = 0.5f
    override val defaultEfficiency: Float = 0f
    override val maxEfficiency: Float = 1f
    override val efficiencyIncreaseFactor: Float = 0.02f
}
