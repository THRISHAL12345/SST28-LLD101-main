Flyweight Pattern — Deduplicate Map Marker Styles

This document explains the Flyweight refactor we performed in the GeoDash map marker system.

I will explain using:

Feynman method (very simple explanation)

Before vs After architecture

Design reasoning

Code refactoring steps

Diagrams

1️⃣ Problem Overview

The CLI tool GeoDash renders a large number of map markers.

Example:

30,000 markers

Each marker has a style:

shape
color
size
filled

Example style:

PIN | RED | 12 | FILLED

But the problem is:

❌ Every marker creates its own style object.

So if we render:

30,000 markers

we might create:

30,000 MarkerStyle objects

Even though most styles are identical.

2️⃣ Why This Is a Problem

Memory usage explodes.

Example:

Markers: 30,000
Unique styles: maybe 96
Objects created: 30,000

This wastes:

CPU
Memory
Garbage collection

The problem exists because style objects are duplicated.

3️⃣ Example of Duplicate Data

Markers:

M1 → PIN RED 12 FILLED
M2 → PIN RED 12 FILLED
M3 → PIN RED 12 FILLED
M4 → PIN RED 12 FILLED

All markers share same style.

But current code does:

new MarkerStyle(...)
new MarkerStyle(...)
new MarkerStyle(...)
new MarkerStyle(...)

Instead we should create:

ONE style object

and reuse it.

4️⃣ Flyweight Pattern Idea (Feynman Explanation)

Imagine a game with trees.

A forest might have:

1 million trees

But most trees share the same properties:

species
color
texture

Instead of storing this inside each tree:

We store it once.

Then each tree just references it.

Tree → position
Tree → position
Tree → position
Tree → shared TreeType

This is exactly the Flyweight pattern.

5️⃣ Key Flyweight Concept

Split data into two types:

Intrinsic State (Shared)

Stored once.

Example:

shape
color
size
filled

These belong to:

MarkerStyle
Extrinsic State (Per Object)

Unique for each marker.

Example:

latitude
longitude
label

These belong to:

MapMarker
6️⃣ Before Refactor (Bad Design)

Each marker owns its own style.

MapMarker
 ├ lat
 ├ lng
 ├ label
 └ MarkerStyle
      ├ shape
      ├ color
      ├ size
      └ filled

Architecture:

MapDataSource
     │
     ▼
  MapMarker
     │
     ▼
new MarkerStyle()

So:

30,000 markers
30,000 styles

Even if styles repeat.

The constructor created styles directly.

this.style = new MarkerStyle(shape,color,size,filled);
7️⃣ After Refactor (Flyweight Design)

Now styles are shared.

MapMarker
 ├ lat
 ├ lng
 ├ label
 └ MarkerStyle (shared reference)

Styles are stored inside:

MarkerStyleFactory

Architecture:

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

Now:

30,000 markers
≤ 96 styles
8️⃣ Flyweight Class Structure

Final architecture:

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

Responsibilities:

Class	Responsibility
MarkerStyle	immutable shared style
MarkerStyleFactory	cache styles
MapMarker	marker position + style reference
MapDataSource	create markers using factory
9️⃣ Step-by-Step Refactoring

We performed four main changes.

🔹 Step 1 — Make MarkerStyle Immutable

Before:

private String shape;
private String color;
private int size;
private boolean filled;

It also had setters.

Problem:

shared object could be modified

So we changed to:

private final String shape;
private final String color;
private final int size;
private final boolean filled;

Removed all setters.

Now style objects cannot change.

This is important because:

multiple markers share the same style
🔹 Step 2 — Create MarkerStyleFactory

Factory stores shared styles.

Map<String, MarkerStyle> cache

Key format:

shape|color|size|filled

Example key:

PIN|RED|12|F

Factory logic:

if style exists
    return cached style
else
    create new style
    store in cache

This ensures one instance per configuration.

🔹 Step 3 — Refactor MapMarker

Before constructor:

MapMarker(lat,lng,label,shape,color,size,filled)

Inside constructor:

new MarkerStyle()

After refactor:

Constructor receives style:

MapMarker(lat,lng,label,style)

This ensures style comes from factory.

Now MapMarker stores only:

lat
lng
label
MarkerStyle reference
🔹 Step 4 — Update MapDataSource

Instead of creating style objects directly:

We call:

factory.get(shape,color,size,filled)

Example:

MarkerStyle style =
    factory.get(shape,color,size,filled);

out.add(new MapMarker(lat,lng,label,style));

Now identical styles are reused.

🔟 Execution Flow

When markers are generated:

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

Then marker is created with that style.

1️⃣1️⃣ Memory Optimization Result

Possible combinations:

Shapes  = 3
Colors  = 4
Sizes   = 4
Filled  = 2

Total styles:

3 × 4 × 4 × 2 = 96

So instead of:

30,000 style objects

we now create:

≤ 96 style objects

Huge memory improvement.

1️⃣2️⃣ QuickCheck Validation

The project contains a QuickCheck class.

It measures unique style object identities.

Before Flyweight:

Markers: 20000
Unique styles: ~20000

After Flyweight:

Markers: 20000
Unique styles: <= 96

This confirms style reuse.

1️⃣3️⃣ Benefits of Flyweight
Memory Efficiency

Huge reduction in object creation.

Performance

Less garbage collection.

Clean Architecture

Separates intrinsic and extrinsic state.

Scalable Design

Supports rendering millions of markers.

1️⃣4️⃣ When Flyweight Pattern Is Used

Typical use cases:

System	Flyweight
Text editors	characters
Game engines	trees, bullets
Maps	markers
GUI systems	icons
caching systems	shared configs

Whenever you see:

many objects with identical state

Flyweight is useful.

1️⃣5️⃣ Final Summary

We refactored the map system to implement Flyweight pattern.

Before:

MapMarker → new MarkerStyle()

Result:

30k style objects

After:

MapMarker → MarkerStyleFactory → shared MarkerStyle

Result:

≤ 96 style objects

Memory usage drops dramatically.

✅ Intrinsic state moved to MarkerStyle
✅ Styles cached by MarkerStyleFactory
✅ MapMarker stores only extrinsic state
✅ System behavior unchanged but optimized# Adapter Pattern — Payment System Refactoring

## 1️⃣ Problem Overview

We are building a payment system inside an `OrderService`.

The system supports multiple payment providers:
- **FastPay**
- **SafeCash**

But their SDK APIs are different.

### FastPay SDK
```java
payNow(customerId, amountCents)
```

### SafeCash SDK
```java
createPayment(amount, user)
confirm()
```

Because of this mismatch, `OrderService` must understand each SDK separately.

That leads to:
- ❌ messy code
- ❌ duplicate logic
- ❌ tight coupling to SDKs
- ❌ difficult to add new providers

---

## 2️⃣ What the Original Code Looked Like

**Before refactoring:**

```
OrderService
   ├── FastPayClient
   └── SafeCashClient
```

Meaning:
- `OrderService` directly knows SDK classes
- It must write special logic for each provider

### Example bad design:
```java
if(provider == "fastpay"){
    fastPay.payNow(...)
}
else if(provider == "safecash"){
    safeCash.createPayment(...).confirm()
}
```

### Problems:

❌ **Violates Open/Closed Principle**
- Adding a new provider means editing `OrderService`

❌ **Tight Coupling**
- `OrderService` depends on SDK classes

❌ **Duplicate Integration Logic**
- Each provider handled differently

---

## 3️⃣ What We Want Instead

We want one common payment interface.

Something like:
```java
charge(customerId, amount)
```

Then all providers should look the same to `OrderService`.

---

## 4️⃣ The Idea of Adapter (Feynman Explanation)

Imagine this real-world example.

You travel from **India to USA**.

Your phone charger has **Indian plug**.

But the **USA socket** is different.

So you use an **Adapter**.

```
Indian Plug → Adapter → US Socket
```

**Adapter converts one format to another.**

### Same concept in software
```
OrderService → PaymentGateway → Adapter → SDK
```

**Adapter translates our interface into SDK calls.**

---

## 5️⃣ New Architecture After Refactor

```
OrderService
      │
      ▼
PaymentGateway (interface)
      │
 ┌────┴─────────┐
 ▼              ▼
FastPayAdapter  SafeCashAdapter
      │              │
      ▼              ▼
FastPayClient   SafeCashClient
```

Now:
- ✅ `OrderService` only talks to `PaymentGateway`
- ✅ Adapters translate to SDK calls

---

## 6️⃣ Intrinsic Idea

Every payment provider must support:
```java
charge(customerId, amountCents)
```

So we create:
```java
public interface PaymentGateway {
    String charge(String customerId, int amountCents);
}
```

Now every adapter implements it.

---

## 7️⃣ What We Implemented

### 1️⃣ Target Interface

**PaymentGateway**

Defines common method:
```java
charge(customerId, amount)
```

### 2️⃣ Adapters

**FastPayAdapter**

Converts:
```
charge()
   ↓
payNow()
```

**SafeCashAdapter**

Converts:
```
charge()
   ↓
createPayment()
confirm()
```

Adapters hide SDK complexity.

### 3️⃣ Map-based Provider Registry

Inside `App`:
```java
Map<String, PaymentGateway> gateways
```

Example:
```java
gateways.put("fastpay", new FastPayAdapter(new FastPayClient()));
gateways.put("safecash", new SafeCashAdapter(new SafeCashClient()));
```

This removes switch statements.

### 4️⃣ OrderService Refactor

**Old idea:**
```java
if(provider == fastpay)
if(provider == safecash)
```

**New approach:**
```java
PaymentGateway gw = gateways.get(provider);
return gw.charge(customerId, amount);
```

Now `OrderService` doesn't know SDKs.

---

## 8️⃣ Before vs After Diagram

### ❌ Before
```
OrderService
   │
   ├── FastPayClient
   │
   └── SafeCashClient
```

**Problems:**
- tight coupling
- SDK logic inside service
- hard to extend

### ✅ After
```
OrderService
      │
      ▼
PaymentGateway
      │
 ┌────┴──────────┐
 ▼               ▼
FastPayAdapter   SafeCashAdapter
      │               │
      ▼               ▼
FastPayClient    SafeCashClient
```

**Advantages:**
- ✔ loose coupling
- ✔ clean architecture
- ✔ new providers easy to add

---

## 9️⃣ How Adding a New Payment Works Now

Suppose we add **Stripe**.

We only write:
```java
StripeAdapter implements PaymentGateway
```

Then register:
```java
gateways.put("stripe", new StripeAdapter(...));
```

**No changes in:**
- `OrderService`

This satisfies **Open/Closed Principle**.

---

## 🔟 Key Design Principles Used

### 1️⃣ Dependency Inversion

**High-level module:**
- `OrderService`

**Depends on:**
- `PaymentGateway` interface

**Not concrete classes.**

### 2️⃣ Open Closed Principle

System is **open** for:
- new payment providers

But **closed** for modification.

### 3️⃣ Single Responsibility

Each class now has a clear job.

| Class | Responsibility |
|-------|----------------|
| `OrderService` | business logic |
| Adapter | SDK translation |
| SDK | external system |

---

## 1️⃣1️⃣ Flow of Execution

When user makes payment:

```
App
 ↓
OrderService
 ↓
PaymentGateway
 ↓
Adapter
 ↓
SDK
```

### Example flow:

```
charge("cust1", 1000)
    ↓
FastPayAdapter
    ↓
FastPayClient.payNow()
```

---

## 1️⃣2️⃣ Final Result

**Output example:**
```
FP#cust-1:1299
SC#pay(cust-2,1299)
```

System behavior remains same, but **architecture is improved**.

---

## 1️⃣3️⃣ What We Learned

**Adapter Pattern is used when:**
- existing classes have incompatible interfaces

**Adapter helps us:**
- convert one interface to another

So that classes can **work together**.

---

## 1️⃣4️⃣ When Adapter Pattern Is Used

Common cases:

| Example | Adapter |
|---------|---------|
| Payment gateways | SDK adapter |
| Database drivers | JDBC adapters |
| Legacy systems | compatibility wrapper |
| File format converters | parser adapters |

---

## ✅ Final Summary

We refactored the payment system using **Adapter Pattern**.

**Before:**
- `OrderService` tightly coupled with SDKs

**After:**
```
OrderService → PaymentGateway → Adapters → SDKs
```

**Benefits:**
- ✔ clean architecture
- ✔ easy to add providers
- ✔ removes conditional logic
- ✔ follows SOLID principles

---

## Build & Run

```bash
cd adapter-payments/src
javac com/example/payments/*.java
java com.example.payments.App
```

### Expected Output:
```
FP#cust-1:1299
SC#pay(cust-2,1299)
```
