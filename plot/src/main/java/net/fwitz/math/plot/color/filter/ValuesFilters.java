package net.fwitz.math.plot.color.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ValuesFilters<V> {
    private final List<? extends ValuesFilter<V>> filters;

    @SafeVarargs
    public ValuesFilters(ValuesFilter<V>... filters) {
        this(Arrays.asList(filters));
    }

    public ValuesFilters(Collection<? extends ValuesFilter<V>> filters) {
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
