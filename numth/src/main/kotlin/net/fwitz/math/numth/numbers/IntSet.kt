package net.fwitz.math.numth.numbers

import java.util.*
import java.util.stream.IntStream

class IntSet private constructor(
    private val bits: BitSet,
    size: Int = bits.length()
) {
    companion object {
        fun ints(values: BitSet): IntSet {
            return IntSet(values.clone() as BitSet)
        }

        fun ints(values: IntSet): IntSet {
            return IntSet(values.bits, values.size)
        }
    }

    private val size: Int = bits.size()

    operator fun iterator(): PrimitiveIterator.OfInt {
        return Itr()
    }

    fun cardinality(): Int {
        return bits.cardinality()
    }

    fun size(): Int {
        return size
    }

    fun stream(): IntStream {
        return bits.stream()
    }

    fun truncate(newSize: Int): IntSet {
        require(newSize >= 0) { "newSize < 0: $newSize" }
        if (newSize >= size) {
            require(newSize <= size) { "newSize > size: $newSize > $size" }
            return this
        }
        return IntSet(bits, newSize)
    }

    private var hash = 0
    override fun hashCode(): Int {
        var h = hash
        if (h == 0) {
            h = bits.hashCode() * 31 + size
            hash = h
        }
        return h
    }

    override fun equals(other: Any?): Boolean {
        return other === this || other is IntSet && equalTo(other)
    }

    private fun equalTo(other: IntSet): Boolean {
        return size == other.size && hashCode() == other.hashCode() && bits == other.bits
    }

    override fun toString(): String {
        return bits.toString()
    }

    private inner class Itr : PrimitiveIterator.OfInt {
        private var next = bits.nextSetBit(0)
        override fun nextInt(): Int {
            val bit = next
            if (bit == -1) {
                throw NoSuchElementException()
            }
            next = bits.nextSetBit(bit + 1)
            return bit
        }

        override fun hasNext(): Boolean {
            return next != -1
        }

        override fun remove() = throw UnsupportedOperationException()
    }
}