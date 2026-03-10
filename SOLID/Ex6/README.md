# 📘 EX6 — LSP: Notification Sender Hierarchy

---

## 1️⃣ Problem Context

We are building a **campus notification system**.

### The system sends notifications through different channels:

- 📧 **Email**
- 📱 **SMS**
- 💬 **WhatsApp**

All senders inherit from the same base class:

```java
NotificationSender
```

Each sender implements:

```java
send(Notification n)
```

### The main program does something like:

```java
NotificationSender sender = new EmailSender();
sender.send(notification);
```

or

```java
NotificationSender sender = new SmsSender();
sender.send(notification);
```

### Expected output:

```
=== Notification Demo ===
EMAIL -> to=riya@sst.edu subject=Welcome body=Hello and welcome to SST!
SMS -> to=9876543210 body=Hello and welcome to SST!
WA ERROR: phone must start with + and country code
AUDIT entries=3
```

---

## 2️⃣ What LSP Means Here

### Liskov Substitution Principle

> **Any subclass should be able to replace the parent class without breaking the program.**

#### Meaning:

If code works with:

```java
NotificationSender
```

Then it should also work with:

- `EmailSender`
- `SmsSender`
- `WhatsAppSender`

**Without unexpected behavior.**

---

## 3️⃣ What Was Wrong

Although all senders inherit from the same base class, they **behave very differently**.

### Problem 1 — EmailSender Changes Message

EmailSender **silently truncates** long messages.

**Example logic:**

```java
if(body.length() > 40)
    body = body.substring(0, 40)
```

This means:

> The caller thinks the full message was sent, but the child class silently modified it.

**This changes the meaning of the message.**

> ❌ **That breaks LSP.**

---

### Problem 2 — WhatsAppSender Adds New Restrictions

WhatsAppSender requires phone numbers to start with:

```
+countrycode
```

**Example:**

```java
if (!phone.startsWith("+"))
    throw exception
```

But the **base class never required this rule**.

So the subclass **tightened the preconditions**.

> ⚠️ **LSP says subclasses must not strengthen preconditions.**

---

### Problem 3 — SMS ignores subject

SMS sender **ignores the subject** completely.

But the base class suggests that a notification has:

```
subject + body
```

**So behavior becomes inconsistent across subclasses.**

---

## 4️⃣ Why This Is Dangerous

Imagine writing this code:

```java
NotificationSender sender = getSender();
sender.send(notification);
```

**Now the developer must ask:**

| Question                      | Reason      |
|-------------------------------|-------------|
| Will the message be truncated?| Email       |
| Will it throw error?          | WhatsApp    |
| Will subject be ignored?      | SMS         |

This means **the base class is unreliable**.

> ❌ **Substitutability is broken.**

---

## 5️⃣ Before Architecture

### ❌ Broken Notification Hierarchy

```
                  NotificationSender
                         |
        -----------------------------------------
        |                |                      |
     EmailSender       SmsSender          WhatsAppSender
 (modifies message)   (ignores subject)  (adds new rule)
```

**Each subclass behaves differently.**

---

## 6️⃣ Refactoring Strategy

We move **shared rules and workflow** into the base class.

### The base class should control:

- ✅ validation
- ✅ message handling
- ✅ audit logging

**Subclasses should only implement delivery logic.**

---

## 7️⃣ After Architecture

### ✅ Consistent Notification Contract

```
                  NotificationSender
                 (controls workflow)
                         |
        -----------------------------------------
        |                |                      |
     EmailSender       SmsSender          WhatsAppSender
   (delivery only)    (delivery only)     (delivery only)
```

**Now subclasses only define how to send, not what rules apply.**

---

## 8️⃣ How the New Design Works

The base class may implement something like:

```java
send(notification):
    validate notification
    normalize data
    doSend(notification)
    record audit
```

Each subclass implements:

```java
doSend()
```

### This ensures:

- ✅ same validation
- ✅ same message rules
- ✅ same behavior guarantees

---

## 9️⃣ Why This Design Is Better

### Predictable behavior

All senders follow the **same contract**.

### Safe substitution

Any sender can replace the base type **safely**.

### Cleaner responsibilities

Base class controls **workflow**; subclasses handle **delivery**.

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine sending letters through different delivery services:

- 📮 Post office
- 🚚 Courier
- ✈️ Express delivery

**All must follow the same rules:**

- ✅ accept the same letter format
- ✅ deliver the same message

**If one service:**

- ❌ cuts your message
- ❌ rejects certain addresses
- ❌ ignores part of the letter

**Then it is not compatible with the system.**

> 💡 **LSP ensures all delivery services behave consistently.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                                  | After                              |
|-----------------------------------------|------------------------------------|
| Subclasses change behavior unpredictably| Base class enforces consistent contract |
|                                         | Subclasses specialize delivery     |

---

## 1️⃣2️⃣ What We Achieved

- ✔️ Consistent sender behavior
- ✔️ No hidden message modifications
- ✔️ No unexpected exceptions
- ✔️ Reliable polymorphism

---

## 🎤 Stage Summary

> **The original senders violated LSP by altering message semantics and strengthening preconditions. We centralized validation and workflow in the base class to ensure consistent behavior across all notification channels.**

---
