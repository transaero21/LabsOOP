package ru.transaero21.mt.core

import ru.transaero21.mt.ammo.Ammunition
import ru.transaero21.mt.core.iterators.AmmoIterator
import ru.transaero21.mt.core.iterators.FighterIterator

data class FighterWrapper(
    val iterator: IteratorWrapper,
    val populateAmmo: (Ammunition) -> Unit
)

data class IteratorWrapper(
    val efIterator: () -> FighterIterator,
    val sfIterator: () -> FighterIterator,
    val eaIterator: () -> AmmoIterator,
    val saIterator: () -> AmmoIterator
)

fun Headquarter.getIteratorWrapper(enemy: Headquarter) = IteratorWrapper(
    efIterator = { FighterIterator(headquarter = enemy) },
    sfIterator = { FighterIterator(headquarter = this) },
    eaIterator = { AmmoIterator(headquarter = enemy) },
    saIterator = { AmmoIterator(headquarter = this) }
)
