# 📊 Singleton Pattern — Metrics Registry Refactoring

This document explains the Singleton refactor we performed in the **PulseMeter** metrics system.

I will explain using:
- **Feynman method** (very simple explanation)
- **Before vs After** architecture
- **Thread safety** mechanisms
- **Protection** against reflection and serialization attacks
- **Diagrams**

---

## 1️⃣ Problem Context

The CLI tool **PulseMeter** maintains runtime metrics such as:

- `REQUESTS_TOTAL`
- `DB_ERRORS`
- `CACHE_HITS`

These metrics must be stored in **one global registry** so every part of the application updates the **same counters**.

However, the starter implementation of `MetricsRegistry` was **not a real Singleton**.

The goal of the refactor was to transform it into a **proper, thread-safe Singleton** that cannot be broken by:
- ❌ concurrency
- ❌ reflection
- ❌ serialization

---

## 2️⃣ Starter Code Problems

The starter implementation allowed **multiple instances** of the registry.

**Example from the starter class:**

```java
private static MetricsRegistry INSTANCE;

public MetricsRegistry() {
}

public static MetricsRegistry getInstance() {
    if (INSTANCE == null) {
        INSTANCE = new MetricsRegistry();
    }
    return INSTANCE;
}
```

This design has **several problems**.

### ❌ Problem 1 — Constructor was Public

Anyone could create a new object:

```java
new MetricsRegistry();
```

This completely **breaks the Singleton rule**.

The loader also directly created one:

```java
MetricsRegistry registry = new MetricsRegistry();
```

*(Seen in the loader implementation)*

```
MetricsLoader
```

So now the system had **multiple registries**.

---

### ❌ Problem 2 — Not Thread Safe

The lazy initialization was unsafe:

```java
if (INSTANCE == null) {
    INSTANCE = new MetricsRegistry();
}
```

If two threads run at the same time, **both could create instances**.

The concurrency tester spawns **80 threads** racing on `getInstance()`.

```
ConcurrencyCheck
```

**Result in broken version:**

```
Unique instances seen: 3
```

Which means **three different registries** existed.

---

### ❌ Problem 3 — Reflection Attack

Reflection can bypass access control:

```java
Constructor<MetricsRegistry> ctor =
    MetricsRegistry.class.getDeclaredConstructor();

ctor.setAccessible(true);
MetricsRegistry evil = ctor.newInstance();
```

The attack example demonstrates this explicitly.

```
ReflectionAttack
```

This creates another object even if we use `getInstance()`.

---

### ❌ Problem 4 — Serialization Breaks Singleton

When Java deserializes an object it **creates a new instance**.

The serialization check shows:

```java
MetricsRegistry a = MetricsRegistry.getInstance();
MetricsRegistry b = deserialize(bytes);
```

Then compares identities.

```
SerializationCheck
```

Starter behavior produces **two different objects**.

---

## 3️⃣ Architecture Before Refactor

```
                 ┌──────────────┐
                 │ MetricsLoader│
                 └──────┬───────┘
                        │
                        ▼
                 new MetricsRegistry()

 App ───────────────► getInstance()
 Thread1 ───────────► getInstance()
 Thread2 ───────────► getInstance()

 Reflection ────────► newInstance()

 Serialization ─────► new object
```

**Result:**

- ❌ Multiple Registry Instances
- ❌ Inconsistent Metrics
- ❌ Broken Singleton

---

## 4️⃣ Refactoring Strategy

To fix the system we enforced **four key rules**.

### Rule 1 — Private Constructor

Prevents external object creation.

### Rule 2 — Lazy Thread-Safe Initialization

Using **Bill Pugh Holder Pattern**.

### Rule 3 — Block Reflection

Throw exception if constructor called twice.

### Rule 4 — Preserve Singleton During Serialization

Use `readResolve()`.

---

## 5️⃣ Architecture After Refactor

```
                getInstance()
                      │
                      ▼
            ┌─────────────────┐
            │ Holder Class     │
            │ static INSTANCE  │
            └─────────┬───────┘
                      │
                      ▼
             ┌─────────────────┐
             │ MetricsRegistry │
             │ private ctor    │
             │ counters map    │
             └─────────────────┘
```

Now **every component receives the same object**.

```
App
Loader
Thread1
Thread2
        │
        ▼
   SAME INSTANCE
```

---

## 6️⃣ What We Changed

| Component | Before | After |
|-----------|--------|-------|
| Constructor | ✗ Public | ✓ Private |
| Instance creation | ✗ `new` everywhere | ✓ `getInstance()` only |
| Thread safety | ✗ Not safe | ✓ Holder pattern |
| Reflection | ✗ Allowed | ✓ Blocked with guard |
| Serialization | ✗ Created new instance | ✓ `readResolve()` returns singleton |
| Loader | ✗ created new registry | ✓ uses singleton |

---

## 7️⃣ Implementation Details

### 🔹 Private Constructor Pattern

**Old:**
```java
public MetricsRegistry() {
}
```

**New:**
```java
private MetricsRegistry() {
    // prevent multiple instantiation
    if (instance already exists) {
        throw new IllegalStateException("Singleton already created");
    }
}
```

---

### 🔹 Bill Pugh Holder Pattern

**Thread-safe lazy initialization:**

```java
private static class Holder {
    private static final MetricsRegistry INSTANCE = new MetricsRegistry();
}

public static MetricsRegistry getInstance() {
    return Holder.INSTANCE;
}
```

**Why this works:**
- JVM guarantees thread safety during class loading
- Lazy initialization (Holder loads only when accessed)
- No synchronization overhead

---

### 🔹 Reflection Protection

Inside constructor:

```java
if (Holder.INSTANCE != null) {
    throw new IllegalStateException("Cannot instantiate via reflection");
}
```

Now reflection attack fails.

---

### 🔹 Serialization Protection

Add method:

```java
protected Object readResolve() {
    return getInstance();
}
```

This ensures deserialization returns the **same singleton instance**.

---

## 8️⃣ Tests That Validate The Fix

### ✅ Concurrency Test

Creates many threads racing on `getInstance()`.

**Expected after fix:**

```
Unique instances seen: 1
```

All threads get the **same instance**.

---

### ✅ Reflection Attack

Attempts to create another instance using reflection.

**Expected after fix:**

```
Exception thrown
Reflection blocked ✓
```

---

### ✅ Serialization Test

Serializes and deserializes the registry.

**Expected after fix:**

```
Same object? true
```

---

## 9️⃣ Feynman Explanation (Super Simple)

### 🏠 Imagine a house with **one notebook** where everyone writes numbers.

**❌ Before:**

```
Mom gets notebook
Dad gets notebook
Brother gets notebook
```

Everyone has **different notebooks**, so numbers don't match.

**✅ After Singleton:**

```
There is ONLY ONE notebook in the house.
Everyone must write in that notebook.
```

No duplicates. All numbers stay consistent.

---

## 🔟 Benefits of Singleton

### ✅ Global Access Point

One instance accessible everywhere.

### ✅ Controlled Initialization

Instance created only once.

### ✅ Thread-Safe

Multiple threads access the same instance safely.

### ✅ Resource Efficiency

No duplicate registry objects.

### ✅ Consistent State

All metrics updated in one place.

---

## 1️⃣1️⃣ When Singleton Pattern Is Used

**Common use cases:**

| System | Singleton |
|--------|-----------|
| Configuration | Config Manager |
| Logging | Logger |
| Database | Connection Pool |
| Caching | Cache Registry |
| Metrics | Metrics Registry |

Whenever you need:
> exactly one instance of a class

**Singleton is the solution.**

---

## 1️⃣2️⃣ Final Result

The refactored design guarantees:

✅ **Exactly one** `MetricsRegistry` instance  
✅ **Thread-safe** access  
✅ Protection against **reflection**  
✅ Protection against **serialization duplication**  
✅ **Consistent global metrics** across the application

This transforms the registry into a **production-grade Singleton**.

---

## 1️⃣3️⃣ Before vs After Summary

**Before:**
```
Multiple MetricsRegistry instances
```

**Problems:**
- ❌ Public constructor
- ❌ Thread-unsafe
- ❌ Reflection vulnerable
- ❌ Serialization breaks singleton

**After:**
```
Single MetricsRegistry instance
```

**Solutions:**
- ✅ Private constructor
- ✅ Bill Pugh Holder pattern
- ✅ Reflection guard
- ✅ readResolve() protection

---

## 🚀 Build & Run

```bash
cd singleton-metrics/src
javac com/example/metrics/*.java
java com.example.metrics.App
```

### Validation Commands

After fixing the implementation, run these checks:

**Concurrency check:**
```bash
java com.example.metrics.ConcurrencyCheck
```

**Reflection attack check:**
```bash
java com.example.metrics.ReflectionAttack
```

**Serialization check:**
```bash
java com.example.metrics.SerializationCheck
```

---

## 📚 Expected Output

All tests should confirm:
- ✅ Single instance across all threads
- ✅ Reflection attack blocked
- ✅ Serialization preserves singleton identity
- ✅ Metrics loaded correctly from `metrics.properties`

---

## 📚 References

- **Design Pattern:** Singleton (Creational)
- **Implementation:** Bill Pugh Holder Pattern
- **Thread Safety:** JVM class loading guarantees
- **Use Case:** Global metrics registry, shared state management
