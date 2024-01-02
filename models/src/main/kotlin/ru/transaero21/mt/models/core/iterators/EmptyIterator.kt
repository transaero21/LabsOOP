package ru.transaero21.mt.models.core.iterators

class EmptyIterator<T>: Iterator<T> {
    override fun hasNext(): Boolean = false
    override fun next(): T = throw NoSuchElementException()

    companion object {
        fun <T> getSafeIterator(getIterator: () -> Iterator<T>): Iterator<T> = try {
            getIterator()
        } catch (e: NoSuchElementException) {
            EmptyIterator<T>().iterator()
        }
    }
}
