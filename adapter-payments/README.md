# 💳 Adapter Pattern — Payments Refactoring

---

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

**Meaning:**
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
> Adding a new provider means editing `OrderService`

❌ **Tight Coupling**
> `OrderService` depends on SDK classes

❌ **Duplicate Integration Logic**
> Each provider handled differently

---

## 3️⃣ What We Want Instead

We want **one common payment interface**.

Something like:
```java
charge(customerId, amount)
```

Then all providers should look the same to `OrderService`.

---

## 4️⃣ The Idea of Adapter (Feynman Explanation)

### 🌍 Imagine this real-world example:

You travel from **India 🇮🇳** to **USA 🇺🇸**.

Your phone charger has **Indian plug** 🔌

But the **USA socket** is different 🔌

So you use an **Adapter** 🔄

```
Indian Plug → Adapter → US Socket
```

**Adapter converts one format to another.**

### Same concept in software 💻
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
       ┌──────┴──────────┐
       ▼                 ▼
  FastPayAdapter   SafeCashAdapter
       │                 │
       ▼                 ▼
  FastPayClient    SafeCashClient
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

**`PaymentGateway`**

Defines common method:
```java
charge(customerId, amount)
```

### 2️⃣ Adapters

#### **FastPayAdapter**

Converts:
```
charge()
   ↓
payNow()
```

#### **SafeCashAdapter**

Converts:
```
charge()
   ↓
createPayment()
confirm()
```

**Adapters hide SDK complexity.**

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

**❌ Old idea:**
```java
if(provider == fastpay)
if(provider == safecash)
```

**✅ New approach:**
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
- ❌ tight coupling
- ❌ SDK logic inside service
- ❌ hard to extend

### ✅ After
```
        OrderService
              │
              ▼
        PaymentGateway
              │
       ┌──────┴───────────┐
       ▼                  ▼
  FastPayAdapter    SafeCashAdapter
       │                  │
       ▼                  ▼
  FastPayClient     SafeCashClient
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
- ✅ `OrderService`

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
- ✅ new payment providers

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

### Common cases:

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
```
OrderService tightly coupled with SDKs
```

**After:**
```
OrderService → PaymentGateway → Adapters → SDKs
```

### **Benefits:**
- ✔ clean architecture
- ✔ easy to add providers
- ✔ removes conditional logic
- ✔ follows SOLID principles

---

## 🚀 Build & Run

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

---

## 📚 References

- **Design Pattern:** Adapter (Structural)
- **Principles:** SOLID, Dependency Inversion, Open/Closed
- **Use Case:** Payment gateway integration, SDK abstraction
