package net.fwitz.math.numbers;

import java.util.BitSet;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public class SmallPrimes {
    private static final int STOP_AT = 1000;
    private static final IntSet SMALL_PRIMES = sieve(STOP_AT);

    public static PrimitiveIterator.OfInt iterator() {
        return SMALL_PRIMES.iterator();
    }

    public static IntStream stream() {
        return SMALL_PRIMES.stream();
    }

    public static IntSet lessThan(int stopAt) {
        if (stopAt <= 2) {
            throw new IllegalArgumentException("stopAt <= 2: " + stopAt);
        }
        return (stopAt > STOP_AT) ? sieve(stopAt) : SMALL_PRIMES.truncate(stopAt);
    }

    private static IntSet sieve(int stopAt) {
        final BitSet primes = new BitSet(stopAt);
        primes.set(2, stopAt);

        int prime = 2;
        do {
            for (int i = prime + prime; i < stopAt; i += prime) {
                primes.clear(i);
            }
            prime = primes.nextSetBit(prime + 1);
        } while (prime != -1);

        return IntSet.ints(primes);
    }

    public static void main(String[] args) {
        System.out.println(SMALL_PRIMES);
    }

    private static class BitSetIterator implements PrimitiveIterator.OfInt {
        private final BitSet bits;
        int next;

        private BitSetIterator(BitSet bits) {
            this.bits = requireNonNull(bits);
            this.next = bits.nextSetBit(0);
        }

        @Override
        public int nextInt() {
            int next = this.next;
            if (next == -1) {
                throw new NoSuchElementException();
            }
            this.next = bits.nextSetBit(next + 1);
            return next;
        }

        @Override
        public boolean hasNext() {
            return next != -1;
        }
    }
}
