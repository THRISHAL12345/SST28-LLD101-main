package com.example.metrics;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class MetricsRegistry implements Serializable {

    private static final long serialVersionUID = 1L;

    private static boolean instanceCreated = false;

    private MetricsRegistry() {
        if (instanceCreated) {
            throw new RuntimeException("Use getInstance() to create MetricsRegistry");
        }
        instanceCreated = true;
    }

    private static class Holder {
        private static final MetricsRegistry INSTANCE = new MetricsRegistry();
    }

    public static MetricsRegistry getInstance() {
        return Holder.INSTANCE;
    }

    private final Map<String, Integer> counters = new ConcurrentHashMap<>();

    public void increment(String key) {
        counters.merge(key, 1, Integer::sum);
    }

    public int getCount(String key) {
        return counters.getOrDefault(key, 0);
    }

    public Map<String, Integer> getAll() {
        return counters;
    }

    private Object readResolve() throws ObjectStreamException {
        return getInstance();
    }
}