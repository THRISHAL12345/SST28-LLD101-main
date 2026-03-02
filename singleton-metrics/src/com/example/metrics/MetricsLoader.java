package com.example.metrics;

import java.io.InputStream;
import java.util.Properties;

public class MetricsLoader {

    public static void load(String fileName) {
        try {
            InputStream input = MetricsLoader.class
                    .getClassLoader()
                    .getResourceAsStream(fileName);

            Properties properties = new Properties();
            properties.load(input);

            MetricsRegistry registry = MetricsRegistry.getInstance();

            for (String key : properties.stringPropertyNames()) {
                int value = Integer.parseInt(properties.getProperty(key));
                for (int i = 0; i < value; i++) {
                    registry.increment(key);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load metrics", e);
        }
    }
}