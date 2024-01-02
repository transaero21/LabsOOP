package ru.transaero21.mt.iterators

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.transaero21.mt.core.iterators.EmptyIterator
import ru.transaero21.mt.core.iterators.EmptyIterator.Companion.getSafeIterator
import ru.transaero21.mt.units.fighters.FieldCommander
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.expect

class EmptyIteratorTest {
    @Test
    fun emptyIteratorAlwaysEmptyTest() {
        val iterator: EmptyIterator<Any> = EmptyIterator()
        assertEquals(expected = false, iterator.hasNext())
        assertThrows<NoSuchElementException> { iterator.next() }
    }

    @Test
    fun getSafeIteratorTest() {
        val getBadIterator = { emptyList<List<Int>>().iterator().next().iterator() }
        assertTrue(actual = getSafeIterator { getBadIterator() } is EmptyIterator)

        val getGoodIterator = { List(size = 10) { listOf(1, 2, 3, 4,5 ) }.iterator().next().iterator() }
        assertTrue(actual = getSafeIterator { getGoodIterator() } !is EmptyIterator)
    }
}
