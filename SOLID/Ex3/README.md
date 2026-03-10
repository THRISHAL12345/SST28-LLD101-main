# 📘 EX3 — OCP: Placement Eligibility Rules Engine

---

## 1️⃣ Problem Context

We are building a **placement eligibility system**.

The system checks whether a student is **eligible for placements** based on rules.

### Example student:

```
Name: Ayaan
CGR: 8.10
Attendance: 72%
Credits: 18
Disciplinary Flag: NONE
```

### Expected output:

```
=== Placement Eligibility ===
Student: Ayaan (CGR=8.10, attendance=72, credits=18, flag=NONE)
RESULT: NOT_ELIGIBLE
- attendance below 75
Saved evaluation for roll=23BCS1001
```

---

## 2️⃣ What Was Wrong (Design Smell)

Inside the engine we had a **long conditional chain:**

```java
if (disciplinaryFlag != NONE)
    reject

else if (cgr < 8)
    reject

else if (attendance < 75)
    reject

else if (credits < 20)
    reject
```

> ⚠️ **Everything was inside one method.**

---

## 3️⃣ Why This Is Bad

Imagine the placement cell adds a **new rule:**

```
Must complete internship
```

**Now we must edit the same method again.**

That means:

| New rule              | Required action   |
|-----------------------|-------------------|
| internship rule       | modify engine     |
| certification rule    | modify engine     |
| project rule          | modify engine     |

**Every new rule requires modifying existing code.**

> ❌ **This breaks OCP.**

---

## 4️⃣ What OCP Means

### Open/Closed Principle

> **Software should be open for extension but closed for modification.**

#### Meaning:

You should be able to **add new behavior** without **editing old code**.

**Old code stays stable.**

---

## 5️⃣ Before Architecture

### ❌ Conditional Rule Engine

```
               +-----------------------+
               |  EligibilityEngine    |
               |-----------------------|
               | if(flag)              |
               | else if(cgr)          |
               | else if(attendance)   |
               | else if(credits)      |
               +-----------+-----------+
                           |
                           v
                    EligibilityResult
```

### Problems:

- ❌ Large conditional chain
- ❌ Hard to extend
- ❌ Risk of breaking existing rules

---

## 6️⃣ Refactoring Strategy

We **convert rules into objects**.

Each rule becomes a **small class**.

### Examples:

- `DisciplinaryRule`
- `CgrRule`
- `AttendanceRule`
- `CreditsRule`

**All rules implement a common interface:**

```java
EligibilityRule
```

---

## 7️⃣ After Architecture

### ✅ Rule-Based Engine

```
                    +----------------------+
                    |   EligibilityEngine  |
                    +----------+-----------+
                               |
                               v
                       List<EligibilityRule>
                               |
       ------------------------------------------------
       |                |               |              |
       v                v               v              v
+---------------+ +-----------+ +---------------+ +-------------+
|Disciplinary   | |CgrRule    | |AttendanceRule | |CreditsRule  |
+---------------+ +-----------+ +---------------+ +-------------+
```

**The engine simply loops over rules.**

---

## 8️⃣ How the New System Works

The engine now does:

```java
For each rule:
    check rule
    if failed → return NOT_ELIGIBLE
```

The engine **does not know rule details** anymore.

**Rules know their own logic.**

---

## 9️⃣ Why This Design Is Better

### 1️⃣ Easy to add rules

To add a new rule:

```java
Create new class implementing EligibilityRule
```

**Engine stays unchanged.**

### 2️⃣ Safer changes

**Old code is untouched.**

Less risk of bugs.

### 3️⃣ Clear rule separation

Each rule is **isolated and testable**.

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine a school principal checking eligibility.

**Before:**

The principal had a **checklist written in one long paragraph:**

```
If discipline issue → reject
Else if CGR low → reject
Else if attendance low → reject
Else if credits low → reject
```

If the school adds a new rule, the principal must **rewrite the paragraph**.

> ❌ **That is messy.**

---

**Now we give the principal rule cards:**

| Card               | Check                |
|--------------------|----------------------|
| Discipline card    | check discipline     |
| CGR card          | check CGR            |
| Attendance card   | check attendance     |
| Credits card      | check credits        |

The principal simply **checks cards one by one**.

**Adding a new rule means adding one new card.**

> 💡 **That is OCP.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                           | After                        |
|----------------------------------|------------------------------|
| EligibilityEngine knows all rules| EligibilityEngine executes rules |
|                                  | Rules contain logic          |

---

## 1️⃣2️⃣ What We Achieved

- ✔️ Extensible rule system
- ✔️ No giant conditionals
- ✔️ Easy to add rules
- ✔️ Cleaner architecture

---

## 🎤 Stage Summary

> **We replaced a conditional chain with a polymorphic rule system, allowing new eligibility rules to be added without modifying the engine.**

---
