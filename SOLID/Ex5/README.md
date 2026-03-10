# 📘 EX5 — LSP: File Exporter Hierarchy

---

## 1️⃣ Problem Context

We are building a **report export system**.

### The system exports reports in different formats:

- 📄 **PDF**
- 📊 **CSV**
- 📋 **JSON**

All exporters extend the same base class:

```java
Exporter
```

### The main program does:

```java
Exporter e = new PdfExporter();
e.export(request);
```

or

```java
Exporter e = new CsvExporter();
e.export(request);
```

### Expected output example:

```
=== Export Demo ===
PDF: ERROR: PDF cannot handle content > 20 chars
CSV: OK bytes=42
JSON: OK bytes=61
```

---

## 2️⃣ What LSP Means

### Liskov Substitution Principle

> **A subclass must be able to replace its parent class without breaking the program.**

#### Meaning:

If code works with:

```java
Exporter
```

Then it must also work correctly with:

- `PdfExporter`
- `CsvExporter`
- `JsonExporter`

**Without surprises.**

---

## 3️⃣ What Was Wrong

Even though all exporters extend the same base class, they **behave very differently**.

### Problem 1 — PDF exporter tightens rules

```java
if (body.length() > 20)
    throw exception
```

But the base class never said:

> "Body must be ≤ 20 characters."

So the child class **added stricter conditions**.

> ❌ **This breaks LSP.**

---

### Problem 2 — CSV exporter changes meaning

CSV exporter **removes commas and newlines**.

**Example:**

Original:
```
Name,Score
Ayaan,82
```

Becomes:
```
Name Score Ayaan 82
```

**The meaning of data changes.**

Caller didn't expect that.

---

### Problem 3 — JSON exporter handles null differently

```java
if (req == null)
    return empty result
```

Other exporters throw error.

**So behavior becomes inconsistent.**

---

## 4️⃣ Why This Is Dangerous

Imagine writing code like:

```java
Exporter exporter = getExporter();
exporter.export(req);
```

**Now you must ask:**

| Question                          | Why       |
|-----------------------------------|-----------|
| Will it throw exception?          | PDF       |
| Will it change data?              | CSV       |
| Will it silently ignore input?    | JSON      |

That means **the base type cannot be trusted**.

> ❌ **LSP is broken.**

---

## 5️⃣ Before Architecture

### ❌ Broken Substitutability

```
                     Exporter
                         |
        ---------------------------------------
        |                |                   |
   PdfExporter       CsvExporter        JsonExporter
  (adds rule)        (changes data)     (different null behavior)
```

**Subclasses behave unpredictably.**

---

## 6️⃣ Refactoring Strategy

We define a **clear contract** in the base class.

### The base class should control:

- ✅ validation
- ✅ null handling
- ✅ overall export workflow

**Children should only handle format-specific logic.**

---

## 7️⃣ After Architecture

### ✅ Stable Contract

```
                    Exporter
                (controls workflow)
                       |
         ---------------------------------
         |               |               |
      PdfExporter     CsvExporter     JsonExporter
     (format only)   (format only)   (format only)
```

**Now subclasses only define format conversion, not core behavior.**

---

## 8️⃣ How the New Design Works

The base class may define something like:

```java
export(request):
    validate request
    normalize null values
    return doExport(request)
```

Child classes only implement:

```java
doExport()
```

### This ensures:

- ✅ Same preconditions
- ✅ Same postconditions
- ✅ Same guarantees

---

## 9️⃣ Why This Design Is Better

### Consistent behavior

All exporters follow the **same rules**.

### Safe substitution

You can replace exporter **safely**.

### Clear contract

Developers know **exactly** how exporters behave.

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine a USB charger.

**All chargers must:**

- 🔌 fit the USB port
- ⚡ provide safe voltage

**If one charger suddenly:**

- ❌ refuses certain phones
- ❌ changes voltage randomly
- ❌ sometimes stops charging

**Then it's not a proper USB charger.**

> The standard is broken.

**LSP ensures all chargers behave according to the same rules.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                           | After                              |
|----------------------------------|------------------------------------|
| Subclasses behave unpredictably  | Base class enforces contract       |
|                                  | Subclasses only specialize behavior|

---

## 1️⃣2️⃣ What We Achieved

- ✔️ Consistent exporter behavior
- ✔️ Safe substitutability
- ✔️ Clear contract enforcement
- ✔️ Predictable system behavior

---

## 🎤 Stage Summary

> **The original exporters violated substitutability by tightening preconditions and altering semantics. We enforced a consistent base contract so all exporters behave predictably.**

---
