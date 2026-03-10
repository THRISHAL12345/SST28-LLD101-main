# 📘 EX4 — OCP: Hostel Fee Calculator

---

## 1️⃣ Problem Context

We are building a **hostel fee calculator**.

### The system must:

- 🏠 Identify room type
- 💰 Add base room price
- ➕ Add optional add-ons
- 🧮 Calculate monthly fee
- 💵 Add deposit
- 🧾 Print receipt

### Example booking:

```
Room: DOUBLE
AddOns: LAUNDRY, MESS
```

### Expected output:

```
=== Hostel Fee Calculator ===
Room: DOUBLE | AddOns: [LAUNDRY, MESS]
Monthly: 16000.00
Deposit: 5000.00
TOTAL DUE NOW: 21000.00
Saved booking: H-7781
```

---

## 2️⃣ What Was Wrong (Design Smell)

Inside the calculator we had:

### Switch for room types

```java
switch(roomType) {
  case SINGLE -> base = 14000
  case DOUBLE -> base = 15000
  case TRIPLE -> base = 12000
}
```

### Conditional logic for add-ons

```java
if (addOn == MESS) add += 1000
else if (addOn == LAUNDRY) add += 500
else if (addOn == GYM) add += 300
```

> ⚠️ **Everything inside one method.**

---

## 3️⃣ Why This Is Bad

Suppose hostel introduces:

- **New room:** `PREMIUM`
- **New add-on:** `WIFI`

**Now we must edit the same calculator again.**

Each new feature requires **modifying old code**.

> ❌ **This breaks OCP.**

---

## 4️⃣ OCP Principle Reminder

### Open/Closed Principle

> **Software entities should be open for extension but closed for modification.**

#### Meaning:

You should **add new types** without **editing existing logic**.

---

## 5️⃣ Before Architecture

### ❌ Switch-Based Design

```
               +-------------------------+
               |  HostelFeeCalculator    |
               |-------------------------|
               | switch(roomType)        |
               | if(addOn==MESS)         |
               | if(addOn==LAUNDRY)      |
               | if(addOn==GYM)          |
               +-----------+-------------+
                           |
                           v
                      BookingReceipt
```

### Problems:

- ❌ New room types require editing code
- ❌ New add-ons require editing code
- ❌ Calculator knows too much

---

## 6️⃣ Refactoring Strategy

We introduce **pricing components**.

### Two abstraction types:

**Room pricing**
- `RoomPricing`

**Add-on pricing**
- `AddOnPricing`

Each type calculates its **own price**.

---

## 7️⃣ After Architecture

### ✅ Polymorphic Pricing System

```
                   +-----------------------+
                   |  HostelFeeCalculator  |
                   +-----------+-----------+
                               |
                               v
                        FeeCalculator
                               |
          ------------------------------------------
          |                                        |
          v                                        v
      RoomPricing                              AddOnPricing
          |                                        |
   ---------------------                -------------------------
   |    |    |    |                     |    |    |
 Single Double Triple Deluxe        Mess Laundry Gym
```

**The calculator now simply adds prices from components.**

---

## 8️⃣ How the New System Works

The fee calculator does something like:

```java
monthly = room.monthlyCharge()

for each addOn:
    monthly += addOn.monthlyCharge()
```

**The calculator no longer cares what type it is.**

---

## 9️⃣ Why This Design Is Better

### 1️⃣ Add new room easily

```java
class PremiumRoom implements RoomPricing
```

**No change to calculator.**

### 2️⃣ Add new add-on easily

```java
class WifiAddOn implements AddOnPricing
```

**Calculator unchanged.**

### 3️⃣ Cleaner responsibilities

| Class            | Responsibility     |
|------------------|--------------------|
| RoomPricing      | room price         |
| AddOnPricing     | add-on price       |
| FeeCalculator    | sum prices         |

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine hostel billing like shopping cart pricing.

**Before:**

The cashier memorizes all prices:

```
If item = apple → price 10
If item = banana → price 5
If item = milk → price 30
```

If new item arrives → cashier must **learn new rule**.

> ❌ **Messy.**

---

**Now we attach price tags to items.**

Each item **knows its own price**.

Cashier simply **scans items**.

Adding new item just means creating a **new product with price tag**.

> 💡 **That is OCP.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                              | After                              |
|-------------------------------------|------------------------------------|
| Calculator knows every room and add-on | Rooms and add-ons know their prices |
|                                     | Calculator just sums them          |

---

## 1️⃣2️⃣ What We Achieved

- ✔️ Removed switch-case logic
- ✔️ Easier extension
- ✔️ Cleaner pricing model
- ✔️ Better modularity

---

## 🎤 Stage Summary

> **We replaced switch-based pricing with polymorphic pricing components, enabling new room types and add-ons without modifying the calculator.**

---
