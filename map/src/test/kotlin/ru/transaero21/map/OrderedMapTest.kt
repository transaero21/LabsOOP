package ru.transaero21.map

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class OrderedMapTest {
    @Test
    fun clearTest() {
        // Fill map with randoms
        val map = OrderedMap<Int, Long>().also { map ->
            repeat(times = 100) {
                map[Random.nextInt()] = Random.nextLong()
            }
        }

        // Check if the card is empty now
        assert(map.isNotEmpty())

        // Clear the card and check that it is now empty
        map.clear()
        assert(map.isEmpty())
    }

    @Test
    fun removeTest() {
        val input = mapOf(321 to 231L, 1 to 6L, 84 to 8L, 97 to 23L, 12 to 5L)
        val map = OrderedMap<Int, Long>().also { map -> map.putAll(input) }

        // Delete an element that exists
        assertEquals(6L, map.remove(key = 1))
        assertEquals(4, map.size)

        // Delete an element that doesn't exist
        assertNull(map.remove(key = 232))
        assertEquals(4, map.size)
    }

    @Test
    fun putAllTest() {
        val input = mapOf(2 to 2L, 1 to 1L, 5 to 5L, 4 to 4L, 3 to 3L)
        val map = OrderedMap<Int, Long>().also { map -> map.putAll(input) }

        assertEquals(5, map.size)
        assertEquals(input.entries.map { it.value }.sorted(), map.entries.map { it.value })
    }

    @Test
    fun putTest() {
        val range = 0 until 10
        val map = OrderedMap<Int, Long>()

        // Add a few elements and check that it's successful
        for (i in range) {
            map[i] = i.toLong() + 10
        }
        assertEquals(10, map.size)

        // Add elements and check that the size has not changed but the values have changed
        for (i in range) {
            assertEquals(i.toLong() + 10, map.put(i, i.toLong()))
        }
        assertEquals(10, map.size)
        assertEquals(List(10) { it.toLong() }, map.entries.map { it.value })
    }

    @Test
    fun getTest() {
        val input = mapOf(6 to 6L, 8 to 8L, 10 to 10L, 12 to 12L, 14 to 14L)
        val map = OrderedMap<Int, Long>().also { map -> map.putAll(input) }

        assertEquals(10L, map[10])
        assertNull(map[9])
    }

    @Test
    fun containsValueTest() {
        val range = 23 until 57
        val map = OrderedMap<Int, Long>().also { map ->
            for (i in range) {
                map[i] = i.toLong()
            }
        }

        assert(map.containsValue(34))
        assert(!map.containsValue(123))
    }

    @Test
    fun containsKeyTest() {
        val range = 87 until 123
        val map = OrderedMap<Int, Long>().also { map ->
            for (i in range) {
                map[i] = i.toLong()
            }
        }
        assert(map.containsKey(122))
        assert(!map.containsValue(83))
    }

    @Test
    fun iteratorTest() {
        val range = 1 until 10 + 1
        val map = OrderedMap<Int, Long>().also { map ->
            for (i in range) {
                map[i] = i.toLong()
            }
        }

        // Check iteration
        var i = range.first
        for (pair in map.entries.iterator()) {
            assertEquals(pair.key, i)
            assertEquals(pair.value, i.toLong())
            // Modify value
            pair.setValue(i.toLong() * 10)
            i += range.step
        }

        // Check iteration after value changes
        i = range.first
        for (pair in map.entries.iterator()) {
            assertEquals(pair.key, i)
            assertEquals(pair.value, i.toLong() * 10)
            i += range.step
        }
    }
}
