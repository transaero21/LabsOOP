package ru.transaero21.map

class OrderedMap<K : Comparable<K>, V>: MutableMap<K, V> {
    /**
     * Holds all key/value pairs in this map.
     */
    private var _entries: MutableList<OrderedMapEntry> = mutableListOf()

    /**
     * Returns a [MutableSet] of all key/value pairs in this map.
     */
    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() = _entries.toMutableSet()

    /**
     * Holds the number of key/value pairs in the map.
     */
    private var _size = 0

    /**
     * Returns the number of key/value pairs in the map.
     */
    override val size: Int get() = _size

    /**
     * Returns a [MutableSet] of all keys in this map.
     */
    override val keys: MutableSet<K> get() = _entries.map { it.key }.toMutableSet()

    /**
     * Returns a [MutableCollection] of all values in this map. Note that this collection may contain duplicate values.
     */
    override val values: MutableCollection<V> get() = _entries.map { it.value }.toMutableList()

    /**
     * Removes all elements from this map.
     */
    override fun clear() {
        _entries = mutableListOf()
        _size = 0
    }

    /**
     * Returns `true` if the map is empty (contains no elements), `false` otherwise.
     */
    override fun isEmpty(): Boolean {
        return _size == 0
    }

    /**
     * Removes the specified key and its corresponding value from this map.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    override fun remove(key: K): V? {
        val i = searchForIndex(key = key)
        val old = if (i < 0) null else _entries[i].value
        if (i >= 0) {
            for (j in i until _size - 1) {
                _entries[j] = _entries[j + 1]
            }
            _size--
        }
        return old
    }

    /**
     * Updates this map with key/value pairs from the specified map [from].
     */
    override fun putAll(from: Map<out K, V>) {
        from.entries.forEach { put(it.key, it.value) }
    }

    /**
     * Associates the specified [value] with the specified [key] in the map.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    override fun put(key: K, value: V): V? {
        val exp = searchForIndex(key = key, approx = true)
        return when {
            _size <= exp -> {
                _entries.add(exp, OrderedMapEntry(pair = key to value))
                _size++
                null
            }
            _entries[exp].key != key -> {
                _entries.add(_entries[_size - 1])
                for (i in size - 1 downTo exp + 1) {
                    _entries[i] = _entries[i - 1]
                }
                _entries[exp] = OrderedMapEntry(pair = key to value)
                _size++
                null
            }
            else -> {
                return _entries[exp].value.let { old ->
                    _entries[exp].setValue(value)
                    old
                }
            }
        }
    }

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the map.
     */
    override operator fun get(key: K): V? {
        val i = searchForIndex(key = key)
        return if (i < 0) null else _entries[i].value
    }

    /**
     * Returns `true` if the map maps one or more keys to the specified [value].
     */
    override fun containsValue(value: V): Boolean {
        return values.contains(value)
    }

    /**
     * Returns `true` if the map contains the specified [key].
     */
    override fun containsKey(key: K): Boolean {
        return keys.contains(key)
    }

    private fun searchForIndex(
        from: Int = 0,
        to: Int = _size - 1,
        key: K,
        approx: Boolean = false
    ): Int {
        if (to >= from) {
            val mid = from + (to - from) / 2
            val entry = _entries[mid]
            return when {
                key < entry.key -> searchForIndex(from = from, to = mid - 1, key = key, approx = approx)
                key > entry.key -> searchForIndex(from = mid + 1, to = to, key = key, approx = approx)
                else -> mid
            }
        }
        return if (approx) from else -1
    }

    /**
     * Represents a key/value pair held by a [OrderedMap].
     */
    inner class OrderedMapEntry(private var pair: Pair<K, V>) : MutableMap.MutableEntry<K, V> {
        /**
         * Returns the key of this key/value pair.
         */
        override val key: K get() = this.pair.first

        /**
         * Returns the value of this key/value pair.
         */
        override val value: V get() = this.pair.second

        /**
         * Changes the value associated with the key of this entry.
         *
         * @return the previous value corresponding to the key.
         */
        override fun setValue(newValue: V): V = pair.also { pair = key to newValue }.second
    }
}
