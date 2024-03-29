package ru.transaero21.mt.models.units.fighters.skills

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter

abstract class Skill: Comparable<Skill> {
    abstract val skillTag: String
    abstract val range: Float
    abstract val timeout: Float

    private var timePassed: Float = 0f

    fun update(delta: Float): Boolean {
        timePassed += delta
        return (timePassed >= timeout).also { if (it) timePassed -= timeout }
    }

    abstract fun useSkill(delta: Float, self: Fighter, fWrapper: FighterWrapper): Boolean

    override fun compareTo(other: Skill): Int {
        return skillTag.compareTo(other.skillTag)
    }

    companion object {
        inline fun <reified T: Skill> Fighter.haveSkill() = skills.firstOrNull { it is T } != null

        inline fun <reified T: Skill> Fighter.getSkill() = skills.firstOrNull { it is T } as? T

        inline fun <reified T: Skill> Fighter.getAndUseSkill(delta: Float, fWrapper: FighterWrapper): Boolean? {
            return this.getSkill<T>()?.useSkill(delta = delta, self = this, fWrapper = fWrapper)
        }
    }
}
