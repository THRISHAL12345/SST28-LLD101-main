package com.example.tickets;

import java.util.regex.Pattern;

public class Validation {

    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z0-9-]{1,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    public static void requireValidId(String id) {
        if (id == null || !ID_PATTERN.matcher(id).matches()) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }

    public static void requireValidEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid Email");
        }
    }

    public static void requireNonEmpty(String value, int max) {
        if (value == null || value.isEmpty() || value.length() > max) {
            throw new IllegalArgumentException("Invalid value");
        }
    }

    public static void requireValidPriority(String priority) {
        if (!priority.matches("LOW|MEDIUM|HIGH|CRITICAL")) {
            throw new IllegalArgumentException("Invalid priority");
        }
    }

    public static void requireValidSLA(Integer sla) {
        if (sla < 5 || sla > 7200) {
            throw new IllegalArgumentException("Invalid SLA");
        }
    }
}