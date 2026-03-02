package com.example.metrics;

import java.io.*;

public class SerializationCheck {

    public static void main(String[] args) throws Exception {

        MetricsRegistry instance1 = MetricsRegistry.getInstance();

        ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("registry.ser"));
        out.writeObject(instance1);
        out.close();

        ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("registry.ser"));
        MetricsRegistry instance2 = (MetricsRegistry) in.readObject();
        in.close();

        System.out.println(instance1.hashCode());
        System.out.println(instance2.hashCode());
    }
}