package ru.transaero21.mt.models.core.iterators

import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.core.iterators.EmptyIterator.Companion.getSafeIterator

class AmmoIterator(headquarter: Headquarter) : Iterator<MutableMap.MutableEntry<Int, out Ammunition>> {
    private var aIterator = getSafeIterator { headquarter.ammunition.iterator() }

    override fun hasNext(): Boolean {
        return aIterator.hasNext()
    }

    override fun next(): MutableMap.MutableEntry<Int, out Ammunition> {
        return if (aIterator.hasNext())
            aIterator.next()
        else throw NoSuchElementException()
    }
}
