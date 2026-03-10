# 📘 EX10 — DIP: Campus Transport Booking

---

## 1️⃣ Problem Context

We are building a **campus ride booking system**.

### When a student books a ride, the system must:

- 📏 Calculate distance
- 🚗 Allocate a driver
- 💳 Charge payment
- 🧾 Print receipt

### Example booking request:

```
Student: 23BCS1010
From: (12.97, 77.59)
To:   (12.93, 77.62)
```

### Expected output:

```
=== Transport Booking ===
DistanceKm=6.0
Driver=DRV-17
Payment=PAID txn=TXN-9001
RECEIPT: R-501 | fare=90.00
```

---

## 2️⃣ What Was Wrong (Design Smell)

Inside the booking service we had:

```java
DistanceCalculator dist = new DistanceCalculator();
DriverAllocator alloc = new DriverAllocator();
PaymentGateway pay = new PaymentGateway();
```

**The `TransportBookingService` directly creates all dependencies.**

> ⚠️ **This causes tight coupling.**

---

## 3️⃣ Why This Is Bad

The booking service is **high-level business logic**.

But it directly depends on **infrastructure systems:**

- 🗺️ GPS distance engine
- 🚗 Driver allocation service
- 💳 Payment gateway

**If any system changes, we must modify the booking service.**

### Example problems:

| Change                         | Required modification    |
|--------------------------------|--------------------------|
| new payment method (UPI)       | edit booking service     |
| new driver algorithm           | edit booking service     |
| new distance calculation       | edit booking service     |

> ❌ **This breaks DIP.**

---

## 4️⃣ What DIP Means (Reminder)

### Dependency Inversion Principle

> **High-level modules should not depend on low-level modules.**
> **Both should depend on abstractions.**

#### Meaning:

**The booking service should depend on interfaces, not concrete implementations.**

---

## 5️⃣ Before Architecture

### ❌ Tight Coupling

```
TransportBookingService
        |
        |---- DistanceCalculator
        |---- DriverAllocator
        |---- PaymentGateway
```

### Problems:

- ❌ Business logic tied to infrastructure
- ❌ Hard to replace systems
- ❌ Hard to test

---

## 6️⃣ Refactoring Strategy

We introduce **abstraction interfaces**.

### Interface Responsibilities

| Interface         | Responsibility         |
|-------------------|------------------------|
| `DistanceCalc`    | calculate trip distance|
| `DriverAllocate`  | assign driver          |
| `PaymentProcess`  | handle payment         |

**Concrete classes implement these interfaces.**

---

## 7️⃣ After Architecture

### ✅ Dependency Inversion

```
               TransportBookingService
                        |
                        v
                 -----------------
                 |   Interfaces  |
                 -----------------
                 | DistanceCalc  |
                 | DriverAllocate|
                 | PaymentProcess|
                 -----------------
                        ^
                        |
        ---------------------------------------
        |                 |                   |
DistanceCalculator   DriverAllocator   PaymentGateway
```

**Now the booking service depends only on abstractions.**

---

## 8️⃣ Dependency Injection

Instead of creating dependencies internally, we **inject them**.

### Example:

```java
TransportBookingService svc =
    new TransportBookingService(
        distanceCalc,
        driverAllocator,
        paymentProcess,
        farePolicy
    );
```

**Now the service only uses capabilities, not implementations.**

---

## 9️⃣ Why This Design Is Better

### Easy to change payment system

**Example:**

`UPIPaymentGateway`

Can replace the old gateway **without modifying booking logic**.

---

### Easy to test

We can inject **mock services:**

- `MockDriverAllocator`
- `MockPaymentGateway`

**So the system can be tested without real drivers or payment systems.**

---

### Clear separation of responsibilities

| Component            | Responsibility          |
|----------------------|-------------------------|
| BookingService       | business logic          |
| DistanceCalculator   | distance calculation    |
| DriverAllocator      | driver assignment       |
| PaymentGateway       | payment processing      |

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine a ride booking manager.

**Before:**

The manager personally calls:

- ❌ the GPS system
- ❌ the driver office
- ❌ the bank payment system

**If any system changes, the manager must change how they work.**

---

**After refactoring:**

The manager simply says:

- ✅ I need someone who can calculate distance
- ✅ I need someone who can allocate a driver
- ✅ I need someone who can process payment

**Different providers can fulfill those roles.**

> 💡 **That's Dependency Inversion.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                                    | After                                  |
|-------------------------------------------|----------------------------------------|
| Booking service depends on concrete infrastructure | Booking service depends on abstractions |
|                                           | Infrastructure plugs into those abstractions |

**Dependency direction is inverted.**

---

## 1️⃣2️⃣ What We Achieved

- ✔️ Loose coupling
- ✔️ Easier testing
- ✔️ Infrastructure flexibility
- ✔️ Clean architecture

---

## 🎤 Stage Summary

> **We inverted dependencies so the booking service depends on abstractions rather than concrete infrastructure systems, allowing flexible replacement of distance, driver, and payment services.**

---
