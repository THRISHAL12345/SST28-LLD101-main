# 📘 EX2 — SRP: Campus Cafeteria Billing

---

## 1️⃣ Problem Context

We are building a **cafeteria billing system**.

### The system:

- 📋 Reads items ordered
- 💰 Looks up menu prices
- 🧮 Calculates subtotal
- 📊 Applies tax
- 🎟️ Applies discount
- 🖨️ Formats invoice
- 💾 Saves invoice

### Example order:

```
Veg Thali x2
Coffee x1
```

### Expected output:

```
=== Cafeteria Billing ===
Invoice# INV-1001
- Veg Thali x2 = 160.00
- Coffee x1 = 30.00
Subtotal: 190.00
Tax(5%): 9.50
Discount: -10.00
TOTAL: 189.50
Saved invoice: INV-1001 (lines=7)
```

---

## 2️⃣ What Was Wrong (Design Smells)

The method:

```java
CafeteriaSystem.checkout()
```

**was doing too many things.**

### Responsibilities mixed together

| Responsibility      | Example                  |
|---------------------|--------------------------|
| Menu lookup         | getting item price       |
| Pricing math        | calculating totals       |
| Tax rules           | student/staff tax        |
| Discount rules      | discount logic           |
| Invoice formatting  | printing invoice         |
| Persistence         | saving invoice           |

> ⚠️ **Everything in one method.**

---

## 3️⃣ Why This Violates SRP

### SRP says:

> **A class should have only one reason to change.**

**But here the system would change if:**

| Change                    | Why code changes |
|---------------------------|------------------|
| Tax policy changes        | edit checkout    |
| Discount rules change     | edit checkout    |
| Invoice format changes    | edit checkout    |
| Storage changes           | edit checkout    |

That means **multiple reasons to change**.

> ❌ **SRP violation.**

---

## 4️⃣ Before Architecture

### ❌ Monolithic Checkout

```
                    +----------------------+
                    |   CafeteriaSystem    |
                    |----------------------|
                    | menu lookup          |
                    | subtotal calculation |
                    | tax rules            |
                    | discount rules       |
                    | invoice formatting   |
                    | persistence          |
                    +----------+-----------+
                               |
                               v
                           FileStore
```

### Problems:

- ❌ Giant method
- ❌ Hard to test
- ❌ Hard to extend
- ❌ Hard to debug

---

## 5️⃣ Refactoring Strategy

We separated responsibilities into independent components.

### Step-by-step extraction

1️⃣ **Extract pricing logic**
   - `PricingService`

2️⃣ **Extract tax policy**
   - `TaxPolicy`
   - `DefaultTaxPolicy`

3️⃣ **Extract discount policy**
   - `DiscountPolicy`
   - `DefaultDiscountPolicy`

4️⃣ **Extract invoice formatting**
   - `InvoicePrinter`

5️⃣ **Extract persistence**
   - `InvoiceRepository`
   - `FileStoreRepository`

**Now the main system only coordinates.**

---

## 6️⃣ After Architecture

### ✅ SRP-Compliant Design

```
                      +----------------------+
                      |   CafeteriaSystem    |
                      |   (orchestrator)     |
                      +----------+-----------+
                                 |
         ---------------------------------------------------------
         |             |              |             |            |
         v             v              v             v            v
+--------------+ +------------+ +--------------+ +--------------+ +----------------+
|PricingService| |TaxPolicy   | |DiscountPolicy| |InvoicePrinter| |InvoiceRepo     |
+--------------+ +------------+ +--------------+ +--------------+ +----------------+
                                                                 |
                                                                 v
                                                              FileStore
```

**Each class now has one responsibility.**

---

## 7️⃣ Why This Design Is Better

### 1️⃣ Clear separation of concerns

Each class focuses on **one task**.

### 2️⃣ Easier testing

Example:

```java
test TaxPolicy independently
```

### 3️⃣ Easy modification

| Change            | Class affected      |
|-------------------|---------------------|
| tax rule          | `TaxPolicy`         |
| discount rule     | `DiscountPolicy`    |
| invoice format    | `InvoicePrinter`    |

### 4️⃣ Safer refactoring

**No giant method risk.**

---

## 8️⃣ Feynman Explanation (Explain Like a Kid)

### Imagine a restaurant counter.

**Originally** one worker did everything:

- 📝 read order
- 💵 calculate price
- 📊 apply tax
- 🎟️ apply discount
- 🧾 print receipt
- 💾 save invoice

The worker gets **overwhelmed**.

---

**So the restaurant hires specialists:**

| Worker              | Job                    |
|---------------------|------------------------|
| PricingService      | calculates subtotal    |
| TaxPolicy           | calculates tax         |
| DiscountPolicy      | applies discount       |
| InvoicePrinter      | prints invoice         |
| Repository          | saves invoice          |

Now the **cashier** (`CafeteriaSystem`) only **coordinates**.

> 💡 **Much easier.**

---

## 9️⃣ Key Takeaways

| Before                         | After                          |
|--------------------------------|--------------------------------|
| One class doing everything     | Multiple focused components    |

### 🔑 SRP Rule

> **If one class handles multiple unrelated concerns, split it.**

---

## 🔟 What We Achieved

- ✔️ Clean architecture
- ✔️ Easier testing
- ✔️ Easier maintenance
- ✔️ Clear responsibilities

---
