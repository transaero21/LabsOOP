package ru.transaero21.mt.models.units.fighters.skills.attacking

import ru.transaero21.mt.models.units.fighters.skills.Skill

abstract class Attacking : Skill() {
    override val skillTag: String = "Attacking"
}
