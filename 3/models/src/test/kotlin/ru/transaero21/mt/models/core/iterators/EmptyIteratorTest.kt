package ru.transaero21.mt.models.core.iterators

import org.junit.jupiter.api.assertThrows
import ru.transaero21.mt.models.core.iterators.EmptyIterator.Companion.getSafeIterator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EmptyIteratorTest {
    @Test
    fun alwaysEmptyTest() {
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
