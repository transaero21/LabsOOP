package ru.transaero21.mt.models.core.iterators

/**
 * An empty iterator implementation that implements the [Iterator] interface.
 * This iterator always returns false for [hasNext] and throws a [NoSuchElementException] for [next].
 */
class EmptyIterator<T>: Iterator<T> {
    /**
     * Always returns false, indicating that there are no more elements in the iteration.
     *
     * @return false
     */
    override fun hasNext(): Boolean = false

    /**
     * Throws a [NoSuchElementException] as there are no elements to return.
     *
     * @throws NoSuchElementException
     */
    override fun next(): T = throw NoSuchElementException()

    companion object {
        /**
         * Safely retrieves an iterator using the provided [getIterator] function. If the [getIterator] function
         * throws a [NoSuchElementException], it returns an instance of [EmptyIterator].
         *
         * @param getIterator A function that returns an iterator.
         * @return An iterator returned by [getIterator], or an instance of [EmptyIterator] if an exception occurs.
         */
        fun <T> getSafeIterator(getIterator: () -> Iterator<T>): Iterator<T> = try {
            getIterator()
        } catch (e: NoSuchElementException) {
            EmptyIterator<T>().iterator()
        }
    }
}
