package net.fwitz.math.numth.numbers

import java.awt.Color
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.IntStream

object Randomizer {
    val RANDOM get() = ThreadLocalRandom.current()

    /**
     * Creates a set of random integers with the given [cardinality][BitSet.cardinality] and
     * maximum allowed [size][BitSet.size].
     *
     *
     * Note: This technique used here is known as known as Floyd's Algorithm and was originally published in
     * [Communications of the ACM](http://cacm.acm.org/magazines/1987/9/10053-programming-pearls).
     * You can also see it discussed
     * [here](http://stackoverflow.com/questions/2394246/algorithm-to-select-a-single-random-combination-of-values).
     *
     *
     * @param cardinality the desired cardinality of the set; must not be larger than `maxSize`
     * @param maxSize     the maximum size permitted
     * @return a bit set initialized as requested
     */
    fun randomBits(cardinality: Int, maxSize: Int): BitSet {
        require(cardinality >= 0) { "cardinality < 0: $cardinality" }
        require(maxSize >= 0) { "maxSize < 0: $maxSize" }
        val bits = BitSet(maxSize)
        if (cardinality == maxSize) {
            bits[0] = maxSize
            return bits
        }
        for (i in cardinality - maxSize until maxSize) {
            val selected = RANDOM.nextInt(i + 1)
            bits.set(if (bits[selected]) i else selected)
        }
        return bits
    }

    fun <T> randomSample(cardinality: Int, supply: Set<T>): Set<T> {
        val result: MutableSet<T> = HashSet((cardinality * 1.5).toInt())
        val selected = randomBits(cardinality, supply.size)
        val iter = supply.iterator()
        var nextIndex = selected.nextSetBit(0)
        val thisIndex = 0
        while (nextIndex != -1) {
            for (i in thisIndex until nextIndex) {
                // skip it
                iter.next()
            }

            // select this item and find the next index that is selected
            result.add(iter.next())
            nextIndex = selected.nextSetBit(nextIndex + 1)
        }
        return result
    }

    fun <K, V> randomSample(cardinality: Int, supply: Map<K, V>): Map<K, V> {
        val result: MutableMap<K, V> = HashMap((cardinality * 1.5).toInt())
        val selected = randomBits(cardinality, supply.size)
        val iter = supply.entries.iterator()
        var nextIndex = selected.nextSetBit(0)
        val thisIndex = 0
        while (nextIndex != -1) {
            for (i in thisIndex until nextIndex) {
                // skip it
                iter.next()
            }

            // select this item and find the next index that is selected
            val (key, value) = iter.next()
            result[key] = value
            nextIndex = selected.nextSetBit(nextIndex + 1)
        }
        return result
    }

    fun randomColor() = Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256))
    fun randomRGB() = randomColor().rgb

    fun shuffledInts(size: Int, random: Random = RANDOM): IntArray {
        val values = IntStream.range(0, size).toArray()
        shuffle(values, random)
        return values
    }

    fun shuffle(values: IntArray, random: Random = RANDOM) {
        for (i in values.size downTo 2) {
            val j = random.nextInt(i)
            val k = i - 1
            val tmp = values[j]
            values[j] = values[k]
            values[k] = tmp
        }
    }

    fun <T> shuffle(list: List<T>, random: Random = RANDOM): List<T> {
        val values = MutableList(list.size) { list[it] }
        for (i in values.size downTo 2) {
            val j = random.nextInt(i)
            val k = i - 1
            val tmp = values[j]
            values[j] = values[k]
            values[k] = tmp
        }
        return values.toList()
    }
}