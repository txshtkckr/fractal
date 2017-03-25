package net.fwitz.math.numth.numbers;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class Randomizer {
    private static final Random RANDOM = new Random();

    /**
     * Creates a set of random integers with the given {@link BitSet#cardinality() cardinality} and
     * maximum allowed {@link BitSet#size() size}.
     * <p>
     * Note: This technique used here is known as known as Floyd's Algorithm and was originally published in
     * <a href="http://cacm.acm.org/magazines/1987/9/10053-programming-pearls">Communications of the ACM</a>.
     * You can also see it discussed
     * <a href="http://stackoverflow.com/questions/2394246/algorithm-to-select-a-single-random-combination-of-values">here</a>.
     * </p>
     *
     * @param cardinality the desired cardinality of the set; must not be larger than {@code maxSize}
     * @param maxSize     the maximum size permitted
     * @return a bit set initialized as requested
     */
    public static BitSet randomBits(final int cardinality, final int maxSize) {
        if (cardinality < 0) {
            throw new IllegalArgumentException("cardinality < 0: " + cardinality);
        }
        if (maxSize < 0) {
            throw new IllegalArgumentException("maxSize < 0: " + maxSize);
        }

        final BitSet bits = new BitSet(maxSize);
        if (cardinality == maxSize) {
            bits.set(0, maxSize);
            return bits;
        }

        for (int i = cardinality - maxSize; i < maxSize; ++i) {
            final int selected = RANDOM.nextInt(i + 1);
            bits.set(bits.get(selected) ? i : selected);
        }
        return bits;
    }

    public static <T> Set<T> randomSample(final int cardinality, final Set<T> supply) {
        final Set<T> result = new HashSet<>((int) (cardinality * 1.5));
        final BitSet selected = randomBits(cardinality, supply.size());
        final Iterator<T> iter = supply.iterator();

        int nextIndex = selected.nextSetBit(0);
        int thisIndex = 0;
        while (nextIndex != -1) {
            for (int i = thisIndex; i < nextIndex; ++i) {
                // skip it
                iter.next();
            }

            // select this item and find the next index that is selected
            result.add(iter.next());
            nextIndex = selected.nextSetBit(nextIndex + 1);
        }
        return result;
    }

    public static <K, V> Map<K, V> randomSample(final int cardinality, final Map<K, V> supply) {
        final Map<K, V> result = new HashMap<>((int) (cardinality * 1.5));
        final BitSet selected = randomBits(cardinality, supply.size());
        final Iterator<Map.Entry<K, V>> iter = supply.entrySet().iterator();

        int nextIndex = selected.nextSetBit(0);
        int thisIndex = 0;
        while (nextIndex != -1) {
            for (int i = thisIndex; i < nextIndex; ++i) {
                // skip it
                iter.next();
            }

            // select this item and find the next index that is selected
            final Map.Entry<K, V> entry = iter.next();
            result.put(entry.getKey(), entry.getValue());
            nextIndex = selected.nextSetBit(nextIndex + 1);
        }
        return result;
    }

    public static int[] shuffledInts(int size) {
        return shuffledInts(size, RANDOM);
    }

    public static int[] shuffledInts(int size, Random random) {
        int[] values = IntStream.range(0, size).toArray();
        shuffle(values, random);
        return values;
    }

    public static void shuffle(int[] values) {
        shuffle(values, RANDOM);
    }

    public static void shuffle(int[] values, Random random) {
        for (int i = values.length; i > 1; --i) {
            int j = random.nextInt(i);
            int k = i - 1;
            int tmp = values[j];
            values[j] = values[k];
            values[k] = tmp;
        }
    }
}
