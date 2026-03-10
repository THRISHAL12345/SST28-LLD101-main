# 📘 EX1 — SRP: Student Onboarding Registration

---

## 1️⃣ Problem Context

We are building a **student onboarding system**.

### Input example:

```
name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE
```

### The system should:

- ✅ Parse the input
- ✅ Validate fields
- ✅ Generate student ID
- ✅ Save to database
- ✅ Print confirmation

### Expected output:

```
=== Student Onboarding ===
INPUT: name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE
OK: created student SST-2026-0001
Saved. Total students: 1
CONFIRMATION:
StudentRecord{id='SST-2026-0001', name='Riya', email='riya@sst.edu', phone='9876543210', program='CSE'}

-- DB DUMP --
| ID             | NAME | PROGRAM |
| SST-2026-0001  | Riya | CSE     |
```

---

## 2️⃣ What Was Wrong (Design Smells)

The main issue was inside:

```java
OnboardingService.registerFromRawInput()
```

**This method was doing too many unrelated things.**

### Responsibilities mixed together

| Responsibility      | Example                  |
|---------------------|--------------------------|
| Input parsing       | splitting raw string     |
| Validation          | checking email, phone    |
| Business logic      | student creation         |
| ID generation       | using IdUtil             |
| Persistence         | saving to FakeDb         |
| Output formatting   | printing messages        |

> ⚠️ **This violates SRP.**

---

## 3️⃣ SRP Principle

### Single Responsibility Principle

> **A class should have only one reason to change.**

#### Meaning:

| If this changes          | Which class should change |
|--------------------------|---------------------------|
| Input format changes     | Parser                    |
| Validation rules change  | Validator                 |
| DB changes               | Repository                |
| Output format changes    | Printer                   |

**But before refactoring:**

👉 One class handled everything

---

## 4️⃣ Before Architecture

### ❌ Monolithic Service

```
                +----------------------+
                |   OnboardingService  |
                |----------------------|
                | parse input          |
                | validate data        |
                | generate ID          |
                | create StudentRecord |
                | save to DB           |
                | print output         |
                +----------+-----------+
                           |
                           v
                        FakeDb
```

### Problems:

- ❌ Too many responsibilities
- ❌ Hard to test
- ❌ Hard to modify
- ❌ Risky to change

---

## 5️⃣ Refactoring Approach

We separated responsibilities into dedicated components.

### Step-by-step extraction

1️⃣ **Extract Parsing**
   - `StudentInputParser`

2️⃣ **Extract Validation**
   - `StudentValidator`

3️⃣ **Extract Persistence**
   - `StudentRepository` (interface)
   - `FakeDbRepository` (implementation)

4️⃣ **Extract Printing**
   - `OnboardingPrinter`

5️⃣ **Keep OnboardingService as workflow coordinator**

---

## 6️⃣ After Architecture

### ✅ Responsibility Separation

```
                       +----------------------+
                       |  OnboardingService   |
                       |  (workflow only)     |
                       +----------+-----------+
                                  |
        ----------------------------------------------------
        |              |             |            |
        v              v             v            v
+--------------+  +--------------+  +-------------+  +----------------+
|Input Parser  |  | Validator    |  |Repository   |  | Printer        |
|--------------|  |--------------|  |-------------|  |----------------|
|parse raw     |  |check rules   |  |save student |  |print results   |
+--------------+  +--------------+  +-------------+  +----------------+
                                         |
                                         v
                                      FakeDb
```

---

## 7️⃣ Why This Design Is Better

### 1️⃣ Clear responsibilities

Each class has **one job**.

### 2️⃣ Easier testing

Example:

```java
test StudentValidator independently
```

### 3️⃣ Easy modification

If email rules change → **only update validator**.

### 4️⃣ Lower coupling

Classes interact via **clean boundaries**.

---

## 8️⃣ Explanation

### Imagine a school admission office.

**Originally** there was one staff member doing everything:

- 📝 reading forms
- ✓ checking details
- 🆔 assigning IDs
- 💾 storing records
- 🖨️ printing confirmation

That staff member gets **overwhelmed**.

---

**So the office hired helpers:**

| Role         | Job                |
|--------------|--------------------|
| Parser       | Reads form         |
| Validator    | Checks details     |
| ID generator | Creates ID         |
| Repository   | Saves student      |
| Printer      | Prints receipt     |

Now the **manager** (`OnboardingService`) only **coordinates**.

> 💡 That is SRP.

---

## 9️⃣ Key Takeaways

| Before                        | After                              |
|-------------------------------|------------------------------------|
| God class doing everything    | Small focused classes collaborating|

### 🔑 SRP Rule

> **If a class changes for multiple unrelated reasons, split it.**

---

## 🔟 What We Achieved

- ✔️ Cleaner architecture
- ✔️ Easier maintenance
- ✔️ Better modularity
- ✔️ Clear separation of concerns

---
