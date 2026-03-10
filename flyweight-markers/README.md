

Proxy Pattern — Secure & Lazy-Load Reports (CampusVault)

This document explains the Proxy pattern refactor we implemented in the CampusVault report viewer system.

We will cover:

Feynman explanation (simple intuition)

Before vs After architecture

Refactoring steps

Lazy loading + access control

Execution flow

Diagrams

1️⃣ Problem Overview

CampusVault is a CLI tool that allows users to open internal reports.

Example users:

Student
Faculty
Admin

Example reports:

Orientation Plan (PUBLIC)
Midterm Review (FACULTY)
Budget Audit (ADMIN)

Each report must enforce access control.

Example:

Role	Allowed Reports
Student	PUBLIC
Faculty	PUBLIC, FACULTY
Admin	PUBLIC, FACULTY, ADMIN
2️⃣ Problems in the Original Design

The starter implementation had several issues.

❌ No Access Control

Any user could open any report.

Example:

Student → Budget Audit

This is a security issue.

❌ No Lazy Loading

Every time a report is opened:

disk read occurs

Even if:

same report opened multiple times
❌ No Caching

Example:

admin opens Budget Audit
admin opens Budget Audit again

Disk loads happen again.

❌ Tight Coupling

The viewer depended directly on the concrete class:

ReportViewer → ReportFile

This prevents flexibility.

3️⃣ What We Want Instead

We want a system where:

✔ access control is enforced
✔ reports load lazily
✔ reports are cached
✔ clients depend on an abstraction

4️⃣ Proxy Pattern (Feynman Explanation)

Imagine a VIP building.

You want to meet someone inside.

You first talk to a security guard.

Visitor → Security Guard → Building

The guard:

1️⃣ checks permission
2️⃣ opens door if allowed
3️⃣ stops unauthorized access

The guard is a Proxy.

Software Equivalent
Client → Proxy → Real Object

Proxy controls access to the real object.

5️⃣ Core Idea of Proxy Pattern

The Proxy implements the same interface as the real object.

So the client cannot tell the difference.

ReportProxy implements Report
RealReport implements Report
6️⃣ Architecture Before Refactor

Original design:

ReportViewer
     │
     ▼
  ReportFile
     │
     ▼
 loadFromDisk()

Problems:

no access control

no caching

disk load every call

tight coupling

Example call flow:

viewer.open(report,user)
      │
      ▼
report.display()
      │
      ▼
loadFromDisk()

Every call loads from disk.

7️⃣ Architecture After Refactor

We introduced a Proxy layer.

Client
  │
  ▼
ReportViewer
  │
  ▼
Report (interface)
  │
  ▼
ReportProxy
  │
  ▼
RealReport

Responsibilities:

Class	Role
Report	abstraction
ReportProxy	access control + lazy loading
RealReport	expensive disk load
ReportViewer	client
8️⃣ Key Components
1️⃣ Report Interface

Defines the common API.

public interface Report {
    void display(User user);
}

Both proxy and real report implement it.

This allows:

polymorphism

Clients only know Report.

2️⃣ RealReport (Real Subject)

This class performs the expensive disk loading.

Responsibilities:

✔ read report from disk
✔ display content

Example behavior:

[disk] loading report R-303 ...

The expensive operation happens here.

3️⃣ ReportProxy (Proxy)

This class sits between client and real object.

Responsibilities:

1️⃣ Access Control

Uses:

AccessControl.canAccess(user, classification)

Example:

Student → ADMIN report

Proxy blocks it.

2️⃣ Lazy Loading

RealReport is created only when needed.

if(realReport == null)
    create RealReport

So the disk load happens only once.

3️⃣ Caching

Proxy stores the loaded report.

private RealReport realReport

Next request uses the cached object.

9️⃣ Access Control System

The system uses role-based rules.

Example logic:

PUBLIC → everyone
FACULTY → faculty/admin
ADMIN → admin only

This is handled by:

AccessControl
🔟 Lazy Loading Mechanism

Lazy loading means:

object is created only when required

Example:

Before request:

realReport = null

First access:

create RealReport
load from disk

Second access:

reuse cached RealReport

No disk load.

1️⃣1️⃣ Execution Flow

Example scenario:

Admin opens Budget Audit

Flow:

ReportViewer
      │
      ▼
ReportProxy.display(user)
      │
      ▼
AccessControl check
      │
      ▼
lazy load RealReport
      │
      ▼
RealReport.display()

Next request:

ReportViewer
      │
      ▼
ReportProxy.display(user)
      │
      ▼
skip load
      │
      ▼
RealReport.display()
1️⃣2️⃣ Example Console Output

Expected output:

=== CampusVault Demo ===

[proxy] request for report R-101
[proxy] lazy loading real report...
[disk] loading report R-101 ...
REPORT -> id=R-101 title=Orientation Plan classification=PUBLIC openedBy=Jasleen
CONTENT: Internal report body for Orientation Plan

[proxy] request for report R-202
[proxy] ACCESS DENIED for Jasleen

[proxy] request for report R-202
[proxy] lazy loading real report...
[disk] loading report R-202 ...
REPORT -> id=R-202 title=Midterm Review classification=FACULTY openedBy=Prof. Noor

[proxy] request for report R-303
[proxy] lazy loading real report...
[disk] loading report R-303 ...
REPORT -> id=R-303 title=Budget Audit classification=ADMIN openedBy=Kshitij

[proxy] request for report R-303
REPORT -> id=R-303 title=Budget Audit classification=ADMIN openedBy=Kshitij

Notice:

disk load happens only once
1️⃣3️⃣ Benefits of Proxy Pattern
Security

Unauthorized access blocked.

Performance

Lazy loading prevents unnecessary disk operations.

Caching

Same report reused.

Loose Coupling

Client depends on abstraction.

Clean Architecture

Responsibilities clearly separated.

1️⃣4️⃣ When Proxy Pattern Is Used

Common real-world uses:

Use Case	Proxy Type
Authentication	Protection Proxy
Lazy loading	Virtual Proxy
Remote services	Remote Proxy
Logging	Smart Proxy
Caching	Caching Proxy

Our example uses:

Protection Proxy + Virtual Proxy
1️⃣5️⃣ Final Summary

We refactored CampusVault using the Proxy pattern.

Before:

ReportViewer → ReportFile

Problems:

no security
no caching
no lazy loading

After:

ReportViewer → ReportProxy → RealReport

Proxy handles:

access control
lazy loading
caching

System behavior remains the same, but design becomes secure, efficient, and scalable.

✅ Introduced Report abstraction
✅ Created RealReport for expensive loading
✅ Implemented ReportProxy for control
✅ Refactored client to depend on interface# Flyweight Pattern — Deduplicate Map Marker Styles

This document explains the Flyweight refactor we performed in the **GeoDash** map marker system.

I will explain using:
- Feynman method (very simple explanation)
- Before vs After architecture
- Design reasoning
- Code refactoring steps
- Diagrams

---

## 1️⃣ Problem Overview

The CLI tool **GeoDash** renders a large number of map markers.

**Example:**
- 30,000 markers

Each marker has a **style**:
- shape
- color
- size
- filled

**Example style:**
```
PIN | RED | 12 | FILLED
```

But the problem is:

❌ **Every marker creates its own style object.**

So if we render:
- 30,000 markers

we might create:
- 30,000 MarkerStyle objects

**Even though most styles are identical.**

---

## 2️⃣ Why This Is a Problem

**Memory usage explodes.**

Example:
- **Markers:** 30,000
- **Unique styles:** maybe 96
- **Objects created:** 30,000

This wastes:
- CPU
- Memory
- Garbage collection

The problem exists because **style objects are duplicated**.

---

## 3️⃣ Example of Duplicate Data

Markers:
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

Imagine a game with trees.

A forest might have:
- 1 million trees

But most trees share the same properties:
- species
- color
- texture

Instead of storing this inside each tree:

**We store it once.**

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

Split data into two types:

### Intrinsic State (Shared)

Stored once.

**Example:**
- shape
- color
- size
- filled

These belong to:
```
MarkerStyle
```

### Extrinsic State (Per Object)

Unique for each marker.

**Example:**
- latitude
- longitude
- label

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
- 30,000 markers
- 30,000 styles

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
- 30,000 markers
- ≤ 96 styles

---

## 8️⃣ Flyweight Class Structure

Final architecture:

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

We performed four main changes.

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
- shared object could be modified

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
- multiple markers share the same style

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
- lat
- lng
- label
- MarkerStyle reference

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
- 30,000 style objects

we now create:
- ≤ 96 style objects

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

### Memory Efficiency

Huge reduction in object creation.

### Performance

Less garbage collection.

### Clean Architecture

Separates intrinsic and extrinsic state.

### Scalable Design

Supports rendering millions of markers.

---

## 1️⃣4️⃣ When Flyweight Pattern Is Used

Typical use cases:

| System | Flyweight |
|--------|-----------|
| Text editors | characters |
| Game engines | trees, bullets |
| Maps | markers |
| GUI systems | icons |
| Caching systems | shared configs |

Whenever you see:
- many objects with identical state

**Flyweight is useful.**

---

## 1️⃣5️⃣ Final Summary

We refactored the map system to implement **Flyweight pattern**.

**Before:**
```
MapMarker → new MarkerStyle()
```

**Result:**
- 30k style objects

**After:**
```
MapMarker → MarkerStyleFactory → shared MarkerStyle
```

**Result:**
- ≤ 96 style objects

**Memory usage drops dramatically.**

✅ Intrinsic state moved to `MarkerStyle`  
✅ Styles cached by `MarkerStyleFactory`  
✅ `MapMarker` stores only extrinsic state  
✅ System behavior unchanged but optimized

---

## Build & Run

```bash
cd flyweight-markers/src
javac com/example/map/*.java
java com.example.map.App
```

### Expected Output:
The system will render all markers with significantly reduced memory usage, reusing style objects efficiently.
