package com.example.metrics;

public class App {
    public static void main(String[] args) {

        MetricsLoader.load("metrics.properties");

        MetricsRegistry registry = MetricsRegistry.getInstance();

        registry.increment("REQUESTS_TOTAL");
        registry.increment("DB_ERRORS");

        System.out.println("REQUESTS_TOTAL = " + registry.getCount("REQUESTS_TOTAL"));
        System.out.println("DB_ERRORS = " + registry.getCount("DB_ERRORS"));
        System.out.println("All metrics = " + registry.getAll());
    }
}