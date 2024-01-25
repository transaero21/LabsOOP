package ru.transaero21.mt.models.core

import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.core.iterators.AmmoIterator
import ru.transaero21.mt.models.core.iterators.FighterIterator

/**
 * Data class representing a wrapper for fighter-related information.
 *
 * @property iterator wrapper for iterators providing access to fighters and ammunition.
 * @property populateAmmo function to populate ammunition in the combat scenario.
 */
data class FighterWrapper(
    val iterator: IteratorWrapper,
    val populateAmmo: (Ammunition) -> Unit
)

/**
 * Data class representing a wrapper for iterators providing access to fighters and ammunition.
 *
 * @property efIterator function to retrieve an enemy fighter iterator.
 * @property sfIterator function to retrieve a self fighter iterator.
 * @property eaIterator function to retrieve an enemy ammunition iterator.
 * @property saIterator function to retrieve a self ammunition iterator.
 */
data class IteratorWrapper(
    val efIterator: () -> FighterIterator,
    val sfIterator: () -> FighterIterator,
    val eaIterator: () -> AmmoIterator,
    val saIterator: () -> AmmoIterator
)
