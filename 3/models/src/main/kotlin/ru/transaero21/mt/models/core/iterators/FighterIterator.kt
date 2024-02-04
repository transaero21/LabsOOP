package ru.transaero21.mt.models.core.iterators

import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.core.iterators.EmptyIterator.Companion.getSafeIterator
import ru.transaero21.mt.models.units.fighters.Fighter

class FighterIterator(headquarter: Headquarter) : Iterator<MutableMap.MutableEntry<Int, out Fighter>> {
    private var fcIterator = getSafeIterator { headquarter.commander.fieldCommanders.entries.iterator() }
    private var fIterator: Iterator<MutableMap.MutableEntry<Int, out Fighter>> = EmptyIterator()

    override fun hasNext(): Boolean {
        return fcIterator.hasNext() || fIterator.hasNext()
    }

    override fun next(): MutableMap.MutableEntry<Int, out Fighter> {
        return if (fIterator.hasNext())
            fIterator.next()
        else if (fcIterator.hasNext())
            fcIterator.next().also { fIterator = getSafeIterator { it.value.formation.fighters.iterator() } }
        else throw NoSuchElementException()
    }
}
