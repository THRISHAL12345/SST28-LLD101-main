# 🗺️ Flyweight Pattern — Deduplicate Map Marker Styles

This document explains the Flyweight refactor we performed in the **GeoDash** map marker system.

I will explain using:
- **Feynman method** (very simple explanation)
- **Before vs After** architecture
- **Design reasoning**
- **Code refactoring steps**
- **Diagrams**

---

## 1️⃣ Problem Overview

The CLI tool **GeoDash** renders a large number of map markers.

**Example:**
- 30,000 markers

Each marker has a **style**:
- `shape`
- `color`
- `size`
- `filled`

**Example style:**
```
PIN | RED | 12 | FILLED
```

But the problem is:

❌ **Every marker creates its own style object.**

So if we render:
- 30,000 markers

we might create:
- 30,000 `MarkerStyle` objects

**Even though most styles are identical.**

---

## 2️⃣ Why This Is a Problem

**Memory usage explodes.**

**Example:**
```
Markers: 30,000
Unique styles: maybe 96
Objects created: 30,000
```

This wastes:
- ❌ CPU
- ❌ Memory
- ❌ Garbage collection

The problem exists because **style objects are duplicated**.

---

## 3️⃣ Example of Duplicate Data

**Markers:**
```
M1 → PIN RED 12 FILLED
M2 → PIN RED 12 FILLED
M3 → PIN RED 12 FILLED
M4 → PIN RED 12 FILLED
```

**All markers share same style.**

But current code does:
```java
new MarkerStyle(...)
new MarkerStyle(...)
new MarkerStyle(...)
new MarkerStyle(...)
```

Instead we should create:
- **ONE style object**

and **reuse it**.

---

## 4️⃣ Flyweight Pattern Idea (Feynman Explanation)

### 🌳 Imagine a game with trees:

A forest might have:
- **1 million trees**

But most trees share the same properties:
- species
- color
- texture

**Instead of storing this inside each tree:**

We **store it once**.

Then each tree just **references** it.

```
Tree → position
Tree → position
Tree → position
Tree → shared TreeType
```

This is exactly the **Flyweight pattern**.

---

## 5️⃣ Key Flyweight Concept

Split data into **two types**:

### Intrinsic State (Shared)

Stored once.

**Example:**
- `shape`
- `color`
- `size`
- `filled`

These belong to:
```
MarkerStyle
```

### Extrinsic State (Per Object)

Unique for each marker.

**Example:**
- `latitude`
- `longitude`
- `label`

These belong to:
```
MapMarker
```

---

## 6️⃣ Before Refactor (Bad Design)

Each marker **owns** its own style.

```
MapMarker
 ├ lat
 ├ lng
 ├ label
 └ MarkerStyle
      ├ shape
      ├ color
      ├ size
      └ filled
```

**Architecture:**

```
MapDataSource
     │
     ▼
  MapMarker
     │
     ▼
new MarkerStyle()
```

So:
- ❌ 30,000 markers
- ❌ 30,000 styles

**Even if styles repeat.**

The constructor created styles directly.

```java
this.style = new MarkerStyle(shape,color,size,filled);
```

---

## 7️⃣ After Refactor (Flyweight Design)

Now styles are **shared**.

```
MapMarker
 ├ lat
 ├ lng
 ├ label
 └ MarkerStyle (shared reference)
```

Styles are stored inside:
```
MarkerStyleFactory
```

**Architecture:**

```
    MapDataSource
          │
          ▼
  MarkerStyleFactory
          │
          ▼
  shared MarkerStyle
          │
          ▼
      MapMarker
```

Now:
- ✅ 30,000 markers
- ✅ ≤ 96 styles

---

## 8️⃣ Flyweight Class Structure

**Final architecture:**

```
           MapDataSource
                │
                ▼
        MarkerStyleFactory
                │
                ▼
           MarkerStyle
        (shared objects)
                │
                ▼
            MapMarker
```

**Responsibilities:**

| Class | Responsibility |
|-------|----------------|
| `MarkerStyle` | immutable shared style |
| `MarkerStyleFactory` | cache styles |
| `MapMarker` | marker position + style reference |
| `MapDataSource` | create markers using factory |

---

## 9️⃣ Step-by-Step Refactoring

We performed **four main changes**.

### 🔹 Step 1 — Make MarkerStyle Immutable

**Before:**
```java
private String shape;
private String color;
private int size;
private boolean filled;
```

It also had setters.

**Problem:**
> shared object could be modified

So we changed to:
```java
private final String shape;
private final String color;
private final int size;
private final boolean filled;
```

**Removed all setters.**

Now style objects **cannot change**.

This is important because:
> multiple markers share the same style

---

### 🔹 Step 2 — Create MarkerStyleFactory

Factory stores shared styles.

```java
Map<String, MarkerStyle> cache
```

**Key format:**
```
shape|color|size|filled
```

**Example key:**
```
PIN|RED|12|F
```

**Factory logic:**
```
if style exists
    return cached style
else
    create new style
    store in cache
```

This ensures **one instance per configuration**.

---

### 🔹 Step 3 — Refactor MapMarker

**Before constructor:**
```java
MapMarker(lat,lng,label,shape,color,size,filled)
```

Inside constructor:
```java
new MarkerStyle()
```

**After refactor:**

Constructor receives style:
```java
MapMarker(lat,lng,label,style)
```

This ensures style comes from factory.

Now `MapMarker` stores only:
- `lat`
- `lng`
- `label`
- `MarkerStyle` reference

---

### 🔹 Step 4 — Update MapDataSource

Instead of creating style objects directly:

We call:
```java
factory.get(shape,color,size,filled)
```

**Example:**
```java
MarkerStyle style =
    factory.get(shape,color,size,filled);

out.add(new MapMarker(lat,lng,label,style));
```

Now **identical styles are reused**.

---

## 🔟 Execution Flow

When markers are generated:

```
    MapDataSource
         │
         ▼
MarkerStyleFactory.get()
         │
         ▼
    check cache
         │
     ┌───┴─────┐
     │         │
    hit       miss
     │         │
  return    create style
  cached    store in cache
  style
```

Then marker is created with that style.

---

## 1️⃣1️⃣ Memory Optimization Result

**Possible combinations:**

```
Shapes  = 3
Colors  = 4
Sizes   = 4
Filled  = 2
```

**Total styles:**
```
3 × 4 × 4 × 2 = 96
```

So instead of:
- ❌ 30,000 style objects

we now create:
- ✅ ≤ 96 style objects

**Huge memory improvement.**

---

## 1️⃣2️⃣ QuickCheck Validation

The project contains a `QuickCheck` class.

It measures **unique style object identities**.

**Before Flyweight:**
```
Markers: 20000
Unique styles: ~20000
```

**After Flyweight:**
```
Markers: 20000
Unique styles: <= 96
```

This confirms **style reuse**.

---

## 1️⃣3️⃣ Benefits of Flyweight

### ✅ Memory Efficiency

Huge reduction in object creation.

### ✅ Performance

Less garbage collection.

### ✅ Clean Architecture

Separates intrinsic and extrinsic state.

### ✅ Scalable Design

Supports rendering millions of markers.

---

## 1️⃣4️⃣ When Flyweight Pattern Is Used

**Typical use cases:**

| System | Flyweight |
|--------|-----------|
| Text editors | characters |
| Game engines | trees, bullets |
| Maps | markers |
| GUI systems | icons |
| Caching systems | shared configs |

Whenever you see:
> many objects with identical state

**Flyweight is useful.**

---

## 1️⃣5️⃣ Final Summary

We refactored the map system to implement **Flyweight pattern**.

**Before:**
```
MapMarker → new MarkerStyle()
```

**Result:**
- ❌ 30k style objects

**After:**
```
MapMarker → MarkerStyleFactory → shared MarkerStyle
```

**Result:**
- ✅ ≤ 96 style objects

**Memory usage drops dramatically.**

### **What Changed:**
- ✅ Intrinsic state moved to `MarkerStyle`
- ✅ Styles cached by `MarkerStyleFactory`
- ✅ `MapMarker` stores only extrinsic state
- ✅ System behavior unchanged but optimized

---

## 🚀 Build & Run

```bash
cd flyweight-markers/src
javac com/example/map/*.java
java com.example.map.App
```

### Expected Output:
The system will render markers with **massive memory savings**, demonstrating how shared style objects reduce memory footprint from thousands of objects to under 100.

---

## 📚 References

- **Design Pattern:** Flyweight (Structural)
- **Key Concept:** Share intrinsic state, externalize extrinsic state
- **Use Case:** Memory optimization, object pooling, shared resources
