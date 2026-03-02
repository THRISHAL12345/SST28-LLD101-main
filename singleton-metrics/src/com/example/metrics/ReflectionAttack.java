package com.example.metrics;

import java.lang.reflect.Constructor;

public class ReflectionAttack {

    public static void main(String[] args) throws Exception {

        MetricsRegistry instance1 = MetricsRegistry.getInstance();

        Constructor<MetricsRegistry> constructor =
                MetricsRegistry.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        try {
            MetricsRegistry instance2 = constructor.newInstance();
            System.out.println("Reflection broke singleton!");
        } catch (Exception e) {
            System.out.println("Reflection blocked successfully!");
        }
    }
}