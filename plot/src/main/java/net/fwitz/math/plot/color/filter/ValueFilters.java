package net.fwitz.math.plot.color.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValueFilters<V> {
    private final List<? extends ValuesFilter<V>> filters;

    @SafeVarargs
    public ValueFilters(ValuesFilter<V>... filters) {
        this(Arrays.asList(filters));
    }

    public ValueFilters(Collection<? extends ValuesFilter<V>> filters) {
        this.filters = Collections.unmodifiableList(new ArrayList<>(filters));
    }

    public int size() {
        return filters.size();
    }

    public ValuesFilter<V> filter(int index) {
        return filters.get(index);
    }

    @Override
    public String toString() {
        return filters.toString();
    }
}
