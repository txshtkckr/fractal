package net.fwitz.math.numbers;

import java.util.BitSet;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

public class IntSet {
    public static IntSet ints(BitSet values) {
        return new IntSet((BitSet) values.clone());
    }

    public static IntSet ints(IntSet values) {
        return new IntSet(values.bits, values.size);
    }

    private final BitSet bits;
    private final int size;

    private int hash;

    private IntSet(BitSet bits) {
        this(bits, bits.length());
    }

    private IntSet(BitSet bits, int size) {
        this.bits = bits;
        this.size = bits.size();
    }

    public PrimitiveIterator.OfInt iterator() {
        return new Itr();
    }

    public int cardinality() {
        return bits.cardinality();
    }

    public int size() {
        return size;
    }

    public IntStream stream() {
        return bits.stream();
    }

    public IntSet truncate(int newSize) {
        if (newSize < 0) {
            throw new IllegalArgumentException("newSize < 0: " + newSize);
        }
        if (newSize >= size) {
            if (newSize > size) {
                throw new IllegalArgumentException("newSize > size: " + newSize + " > " + size);
            }
            return this;
        }
        return new IntSet(bits, newSize);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0) {
            h = bits.hashCode() * 31 + size;
            hash = h;
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || (o instanceof IntSet && equalTo((IntSet) o));
    }

    private boolean equalTo(IntSet other) {
        return size == other.size && hashCode() == other.hashCode() && bits.equals(other.bits);
    }

    @Override
    public String toString() {
        return bits.toString();
    }
    

    private class Itr implements PrimitiveIterator.OfInt {
        private int next = bits.nextSetBit(0);

        @Override
        public int nextInt() {
            final int bit = next;
            if (bit == -1) {
                throw new NoSuchElementException();
            }
            next = bits.nextSetBit(bit + 1);
            return bit;
        }

        @Override
        public boolean hasNext() {
            return next != -1;
        }
    }
}
