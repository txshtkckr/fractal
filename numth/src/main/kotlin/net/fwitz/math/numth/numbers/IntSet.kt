package net.fwitz.math.numth.numbers

import java.util.*

class IntSet private constructor(
    private val bits: BitSet,
    val size: Int = bits.length()
): Sequence<Int> {
    companion object {
        fun bits(bits: BitSet, size: Int = bits.length()): IntSet {
            val copy = bits.clone() as BitSet
            val firstBitToTrash = copy.nextSetBit(size)
            if (firstBitToTrash != -1) copy.clear(firstBitToTrash, copy.length())
            return IntSet(copy, size)
        }
    }

    override operator fun iterator(): PrimitiveIterator.OfInt = Itr()

    val cardinality get() = bits.cardinality()
    val stream get() = bits.stream()
    val seq get() = iterator().asSequence()

    fun orInto(bits: BitSet) = bits.or(this.bits)
    fun andInto(bits: BitSet) = bits.and(this.bits)

    fun truncate(newSize: Int): IntSet {
        require(newSize >= 0) { "newSize < 0: $newSize" }
        if (newSize >= size) {
            require(newSize == size) { "newSize > size: $newSize > $size" }
            return this
        }

        val firstBitOutOfRange = bits.nextSetBit(newSize)
        if (firstBitOutOfRange == -1) return this

        val copy = bits.clone() as BitSet
        copy.clear(firstBitOutOfRange, size)
        return IntSet(copy, newSize)
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

    override fun toString() = bits.toString()

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