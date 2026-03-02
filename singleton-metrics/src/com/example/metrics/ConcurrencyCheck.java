package com.example.metrics;

public class ConcurrencyCheck {

    public static void main(String[] args) throws Exception {

        Runnable task = () -> {
            MetricsRegistry instance = MetricsRegistry.getInstance();
            System.out.println(instance.hashCode());
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}