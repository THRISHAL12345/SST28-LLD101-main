# 📘 EX9 — DIP: Assignment Evaluation Pipeline

---

## 1️⃣ Problem Context

We are building a **student assignment evaluation pipeline**.

### When a submission arrives, the system must:

- 🔍 Check plagiarism
- 📊 Grade the code
- 📝 Write an evaluation report
- 🖨️ Print final result

### Example submission:

```
Student: 23BCS1007
Code: public class A{}
File: A.java
```

### Expected output:

```
=== Evaluation Pipeline ===
PlagiarismScore=12
CodeScore=78
Report written: report-23BCS1007.txt
FINAL: PASS (total=90)
```

---

## 2️⃣ What Was Wrong (Design Smell)

Inside the pipeline we had code like:

```java
Rubric rubric = new Rubric();
PlagiarismChecker pc = new PlagiarismChecker();
CodeGrader grader = new CodeGrader();
ReportWriter writer = new ReportWriter();
```

**The pipeline directly created all components.**

> ⚠️ **This means the pipeline depends on concrete classes.**

---

## 3️⃣ Why This Is Bad

The pipeline is **high-level business logic**.

But it depends on **low-level implementations**.

### Example problems:

| Change                  | Required action   |
|-------------------------|-------------------|
| new plagiarism tool     | modify pipeline   |
| new grading algorithm   | modify pipeline   |
| new report format       | modify pipeline   |

> ❌ **Every change requires editing the pipeline.**

---

## 4️⃣ What DIP Means

### Dependency Inversion Principle

> **High-level modules should not depend on low-level modules.**
> **Both should depend on abstractions.**

#### Meaning:

**The pipeline should depend on interfaces, not concrete classes.**

---

## 5️⃣ Before Architecture

### ❌ Tight Coupling

```
             EvaluationPipeline
                      |
      --------------------------------------
      |            |            |
PlagiarismChecker CodeGrader ReportWriter
```

**The pipeline knows exact implementations.**

### Problems:

- ❌ Hard to test
- ❌ Hard to extend
- ❌ Hard to replace components

---

## 6️⃣ Refactoring Strategy

We introduce **abstraction interfaces**.

### Interface	Purpose

| Interface         | Purpose                    |
|-------------------|----------------------------|
| `PlagiarismCheck` | check plagiarism           |
| `CodeGrade`       | grade assignment           |
| `ReportWrite`     | write evaluation report    |

**Concrete classes implement these interfaces.**

---

## 7️⃣ After Architecture

### ✅ Dependency Inversion

```
                EvaluationPipeline
                       |
                       v
                ----------------
                |  Interfaces  |
                ----------------
                | PlagiarismCheck |
                | CodeGrade       |
                | ReportWrite     |
                ----------------
                       ^
                       |
        ------------------------------------
        |                |                |
 PlagiarismChecker   CodeGrader       ReportWriter
```

**Now the pipeline depends only on interfaces.**

---

## 8️⃣ Dependency Injection

Instead of creating objects internally, we **inject them**.

### Example:

```java
EvaluationPipeline pipeline =
    new EvaluationPipeline(
        plagiarismChecker,
        codeGrader,
        reportWriter
    );
```

**Now the pipeline only uses capabilities.**

---

## 9️⃣ Why This Design Is Better

### Easy to replace components

**Example:**

`AIPlagiarismChecker`

Can replace the old checker **without touching the pipeline**.

---

### Easy to test

We can inject **mock implementations**.

**Example:**

```java
MockPlagiarismChecker
```

For testing pipeline logic.

---

### Clear architecture

**Business logic depends on abstractions, not infrastructure.**

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine a school principal evaluating assignments.

**Before:**

The principal personally chooses:

- ❌ which plagiarism software
- ❌ which grader
- ❌ which report generator

**If a tool changes, the principal must change their workflow.**

---

**After refactoring:**

The principal only says:

- ✅ I need someone who can check plagiarism
- ✅ I need someone who can grade code
- ✅ I need someone who can write reports

**Different specialists can fill these roles.**

> 💡 **That's DIP.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                                  | After                                |
|-----------------------------------------|--------------------------------------|
| Pipeline depends on concrete implementations | Pipeline depends on interfaces  |
|                                         | Implementations plug into those interfaces |

**Dependency direction is inverted.**

---

## 1️⃣2️⃣ What We Achieved

- ✔️ Loose coupling
- ✔️ Easy testing
- ✔️ Easy component replacement
- ✔️ Clean architecture

---

## 🎤 Stage Summary

> **We inverted dependencies so the evaluation pipeline now depends on abstractions instead of concrete grading and reporting implementations.**

---
