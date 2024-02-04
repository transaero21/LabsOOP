package ru.transaero21.mt.models.units.fighters.skills.healing

import ru.transaero21.mt.models.units.fighters.skills.Skill

abstract class Healing : Skill() {
    override val skillTag: String = "Healing"
}
