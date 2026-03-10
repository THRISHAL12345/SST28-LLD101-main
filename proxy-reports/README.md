# Proxy Pattern — Secure & Lazy-Load Reports (CampusVault)

This document explains the Proxy pattern refactor we implemented in the **CampusVault** report viewer system.

We will cover:
- Feynman explanation (simple intuition)
- Before vs After architecture
- Refactoring steps
- Lazy loading + access control
- Execution flow
- Diagrams

---

## 1️⃣ Problem Overview

**CampusVault** is a CLI tool that allows users to open internal reports.

**Example users:**
- Student
- Faculty
- Admin

**Example reports:**
- Orientation Plan (PUBLIC)
- Midterm Review (FACULTY)
- Budget Audit (ADMIN)

Each report must **enforce access control**.

**Example:**

| Role | Allowed Reports |
|------|----------------|
| Student | PUBLIC |
| Faculty | PUBLIC, FACULTY |
| Admin | PUBLIC, FACULTY, ADMIN |

---

## 2️⃣ Problems in the Original Design

The starter implementation had several issues.

### ❌ No Access Control

Any user could open any report.

**Example:**
```
Student → Budget Audit
```

This is a **security issue**.

### ❌ No Lazy Loading

Every time a report is opened:
- disk read occurs

Even if:
- same report opened multiple times

### ❌ No Caching

**Example:**
```
admin opens Budget Audit
admin opens Budget Audit again
```

Disk loads happen **again**.

### ❌ Tight Coupling

The viewer depended directly on the concrete class:

```
ReportViewer → ReportFile
```

This prevents flexibility.

---

## 3️⃣ What We Want Instead

We want a system where:

✔ access control is enforced  
✔ reports load lazily  
✔ reports are cached  
✔ clients depend on an abstraction

---

## 4️⃣ Proxy Pattern (Feynman Explanation)

Imagine a **VIP building**.

You want to meet someone inside.

You first talk to a **security guard**.

```
Visitor → Security Guard → Building
```

The guard:

1️⃣ checks permission  
2️⃣ opens door if allowed  
3️⃣ stops unauthorized access

The guard is a **Proxy**.

### Software Equivalent
```
Client → Proxy → Real Object
```

Proxy **controls access** to the real object.

---

## 5️⃣ Core Idea of Proxy Pattern

The Proxy implements the **same interface** as the real object.

So the client **cannot tell the difference**.

```
ReportProxy implements Report
RealReport implements Report
```

---

## 6️⃣ Architecture Before Refactor

Original design:

```
ReportViewer
     │
     ▼
  ReportFile
     │
     ▼
 loadFromDisk()
```

**Problems:**
- no access control
- no caching
- disk load every call
- tight coupling

**Example call flow:**

```
viewer.open(report,user)
      │
      ▼
report.display()
      │
      ▼
loadFromDisk()
```

Every call loads from disk.

---

## 7️⃣ Architecture After Refactor

We introduced a **Proxy layer**.

```
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
```

**Responsibilities:**

| Class | Role |
|-------|------|
| `Report` | abstraction |
| `ReportProxy` | access control + lazy loading |
| `RealReport` | expensive disk load |
| `ReportViewer` | client |

---

## 8️⃣ Key Components

### 1️⃣ Report Interface

Defines the common API.

```java
public interface Report {
    void display(User user);
}
```

Both proxy and real report implement it.

This allows:
- polymorphism

Clients only know `Report`.

### 2️⃣ RealReport (Real Subject)

This class performs the **expensive disk loading**.

**Responsibilities:**
- ✔ read report from disk
- ✔ display content

**Example behavior:**
```
[disk] loading report R-303 ...
```

The expensive operation happens here.

### 3️⃣ ReportProxy (Proxy)

This class sits between client and real object.

**Responsibilities:**

#### 1️⃣ Access Control

Uses:
```java
AccessControl.canAccess(user, classification)
```

**Example:**
```
Student → ADMIN report
```

Proxy blocks it.

#### 2️⃣ Lazy Loading

`RealReport` is created **only when needed**.

```java
if(realReport == null)
    create RealReport
```

So the disk load happens **only once**.

#### 3️⃣ Caching

Proxy stores the loaded report.

```java
private RealReport realReport
```

Next request uses the **cached object**.

---

## 9️⃣ Access Control System

The system uses **role-based rules**.

**Example logic:**

```
PUBLIC → everyone
FACULTY → faculty/admin
ADMIN → admin only
```

This is handled by:
```
AccessControl
```

---

## 🔟 Lazy Loading Mechanism

Lazy loading means:
- object is created **only when required**

**Example:**

**Before request:**
```
realReport = null
```

**First access:**
```
create RealReport
load from disk
```

**Second access:**
```
reuse cached RealReport
```

No disk load.

---

## 1️⃣1️⃣ Execution Flow

**Example scenario:**

Admin opens Budget Audit

**Flow:**

```
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
```

**Next request:**

```
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
```

---

## 1️⃣2️⃣ Example Console Output

**Expected output:**

```
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
```

**Notice:**
- disk load happens **only once**

---

## 1️⃣3️⃣ Benefits of Proxy Pattern

### Security

Unauthorized access blocked.

### Performance

Lazy loading prevents unnecessary disk operations.

### Caching

Same report reused.

### Loose Coupling

Client depends on abstraction.

### Clean Architecture

Responsibilities clearly separated.

---

## 1️⃣4️⃣ When Proxy Pattern Is Used

Common real-world uses:

| Use Case | Proxy Type |
|----------|------------|
| Authentication | Protection Proxy |
| Lazy loading | Virtual Proxy |
| Remote services | Remote Proxy |
| Logging | Smart Proxy |
| Caching | Caching Proxy |

Our example uses:
```
Protection Proxy + Virtual Proxy
```

---

## 1️⃣5️⃣ Final Summary

We refactored **CampusVault** using the Proxy pattern.

**Before:**
```
ReportViewer → ReportFile
```

**Problems:**
- no security
- no caching
- no lazy loading

**After:**
```
ReportViewer → ReportProxy → RealReport
```

**Proxy handles:**
- access control
- lazy loading
- caching

System behavior remains the same, but design becomes **secure, efficient, and scalable**.

✅ Introduced `Report` abstraction  
✅ Created `RealReport` for expensive loading  
✅ Implemented `ReportProxy` for control  
✅ Refactored client to depend on interface

---

## Build & Run

```bash
cd proxy-reports/src
javac com/example/reports/*.java
java com.example.reports.App
```

### Expected Output:
The system will demonstrate access control enforcement, lazy loading on first access, and caching for subsequent requests.
