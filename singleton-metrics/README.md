📘 Exercise 2 — Immutable Classes (Incident Tickets)
1. Problem Context

The CLI tool HelpLite manages support tickets.

Each ticket represents an incident report containing fields like:

ticket id

reporter email

title

description

priority

tags

assignee

SLA minutes

source

A ticket should behave like a record of an event.

Once created, it should never change, because changes can break:

audit logs

debugging history

distributed systems consistency

However, the starter implementation made IncidentTicket mutable.

2. Starter Code Problems

The original class allowed tickets to change after creation.

Example fields:

private String id;
private String reporterEmail;
private String title;

These fields were not final, meaning they could be modified.

The class also exposed public setters:

public void setPriority(String priority) { this.priority = priority; }
public void setTags(List<String> tags) { this.tags = tags; }

(From the starter implementation) 

IncidentTicket

This allowed the ticket to mutate anytime.

❌ Problem 1 — Mutable Fields

Example scenario:

IncidentTicket t = service.createTicket(...);
t.setPriority("CRITICAL");

Now the original ticket is different from when it was created.

This breaks auditability.

❌ Problem 2 — Service Mutates Tickets

The service layer modified tickets after creation.

Example:

t.setPriority("MEDIUM");
t.setSource("CLI");

(Shown in the service implementation) 

TicketService

Then escalation mutates again:

t.setPriority("CRITICAL");
t.getTags().add("ESCALATED");

Now the ticket keeps changing over time.

❌ Problem 3 — Internal List Leakage

The getter returned the real list:

public List<String> getTags() { return tags; }

This allows outside code to do:

ticket.getTags().add("HACKED");

Which modifies the ticket from outside.

❌ Problem 4 — Validation Was Scattered

Validation was done in multiple places.

Example:

if (reporterEmail == null || !reporterEmail.contains("@"))

(Seen in the service layer) 

TicketService

This leads to:

inconsistent validation

missed checks

duplicated logic

3. Architecture Before Refactor
                TicketService
                     │
                     ▼
             createTicket()
                     │
                     ▼
               IncidentTicket
              (mutable object)
                     │
                     ▼
     setters mutate object after creation

Example flow:

createTicket()
    ↓
setPriority()
    ↓
assign()
    ↓
external code modifies tags

Result:

Ticket state constantly changes ❌
Audit trail unreliable ❌
External code can modify state ❌
4. Refactoring Strategy

To fix the design we applied Immutable Object principles.

Rule 1 — Make Class Immutable

All fields become private final.

Rule 2 — Remove Setters

No field can change after construction.

Rule 3 — Use Builder Pattern

Allows constructing complex objects safely.

Rule 4 — Defensive Copying

Prevent outside modification of collections.

Rule 5 — Centralize Validation

All validation happens in Builder.build().

5. Architecture After Refactor
                TicketService
                     │
                     ▼
             IncidentTicket.Builder
                     │
                     ▼
                   build()
                     │
                     ▼
             Immutable IncidentTicket

Now the ticket cannot change.

If we want an update:

oldTicket → builder → newTicket
6. Builder Pattern Design

Instead of many constructors:

new IncidentTicket(id, email, title)

We now use:

IncidentTicket ticket =
    IncidentTicket.builder()
        .id("TCK-1001")
        .reporterEmail("user@example.com")
        .title("Payment failure")
        .priority("HIGH")
        .build();

Builder advantages:

readable

flexible

validation before creation

7. Defensive Copying for Tags

To prevent external mutation we copy the list.

Instead of:

this.tags = tags;

We do:

this.tags = Collections.unmodifiableList(new ArrayList<>(tags));

Now this fails:

ticket.getTags().add("HACKED");

The ticket stays safe.

8. Centralized Validation

All checks happen inside build().

Examples include:

ticket id format

email format

title length

allowed priorities

SLA range

Validation helpers are provided in a dedicated class. 

Validation

This ensures one single validation point.

9. Updating Tickets (New Instance)

Because the ticket is immutable, updates create new objects.

Example:

IncidentTicket updated =
    oldTicket.toBuilder()
        .priority("CRITICAL")
        .build();

Now we have:

oldTicket  → unchanged
newTicket  → updated
10. Demonstration

The demo program shows why mutability was dangerous.

Example code:

List<String> tags = t.getTags();
tags.add("HACKED_FROM_OUTSIDE");

(Shown in the demo) 

TryIt

After refactor this will no longer work, because the list is immutable.

11. Before vs After Summary
Feature	Before	After
Fields	Mutable	final
Setters	Present	Removed
Validation	Scattered	Centralized
List exposure	Leaked	Defensive copy
Object state	Changes	Immutable
Updates	Mutate object	Create new object
12. Feynman Explanation (Super Simple)

Imagine writing a receipt.

Before:

Write receipt
Then erase parts
Rewrite values
Erase again

The receipt keeps changing.

After immutability:

Receipt printed.
You cannot change it.
If you need change → print a new receipt.

So:

Old ticket stays the same
New ticket contains the update
13. Benefits of Immutability

Thread-safe by default

Easier debugging

Reliable audit logs

Safe for distributed systems

Prevents accidental mutation

This is why many important Java classes are immutable:

String

Integer

LocalDate

14. Final Result

The system now ensures:

tickets cannot change after creation

validation happens once during build

external code cannot modify internal state

updates produce new ticket instances

This results in predictable, safe, and maintainable ticket objects.# Singleton Refactoring (Metrics Registry)

## 1. Problem Context

The CLI tool **PulseMeter** maintains runtime metrics such as:

- `REQUESTS_TOTAL`
- `DB_ERRORS`
- `CACHE_HITS`

These metrics must be stored in **one global registry** so every part of the application updates the same counters.

However, the starter implementation of `MetricsRegistry` was **not a real Singleton**.

The goal of the refactor was to transform it into a **proper, thread-safe Singleton** that cannot be broken by:

- concurrency
- reflection
- serialization

---

## 2. Starter Code Problems

The starter implementation allowed **multiple instances** of the registry.

Example from the starter class:

```java
private static MetricsRegistry INSTANCE;

public MetricsRegistry() {
}

public static MetricsRegistry getInstance() {
    if (INSTANCE == null) {
        INSTANCE = new MetricsRegistry();
    }
    return INSTANCE;
}
```

This design has several problems.

### ❌ Problem 1 — Constructor was Public

Anyone could create a new object:

```java
new MetricsRegistry();
```

This completely **breaks the Singleton rule**.

The loader also directly created one:

```java
MetricsRegistry registry = new MetricsRegistry();
```

(Seen in the loader implementation)

```
MetricsLoader
```

So now the system had **multiple registries**.

### ❌ Problem 2 — Not Thread Safe

The lazy initialization was unsafe:

```java
if (INSTANCE == null) {
    INSTANCE = new MetricsRegistry();
}
```

If two threads run at the same time, **both could create instances**.

The concurrency tester spawns 80 threads racing on `getInstance()`.

```
ConcurrencyCheck
```

**Result in broken version:**

```
Unique instances seen: 3
```

Which means **three different registries** existed.

### ❌ Problem 3 — Reflection Attack

Reflection can bypass access control:

```java
Constructor<MetricsRegistry> ctor =
    MetricsRegistry.class.getDeclaredConstructor();

ctor.setAccessible(true);
MetricsRegistry evil = ctor.newInstance();
```

The attack example demonstrates this explicitly.

```
ReflectionAttack
```

This creates another object even if we use `getInstance()`.

### ❌ Problem 4 — Serialization Breaks Singleton

When Java deserializes an object it **creates a new instance**.

The serialization check shows:

```java
MetricsRegistry a = MetricsRegistry.getInstance();
MetricsRegistry b = deserialize(bytes);
```

Then compares identities.

```
SerializationCheck
```

Starter behavior produces **two different objects**.

---

## 3. Architecture Before Refactor

```
                 ┌──────────────┐
                 │ MetricsLoader│
                 └──────┬───────┘
                        │
                        ▼
                 new MetricsRegistry()

 App ───────────────► getInstance()
 Thread1 ───────────► getInstance()
 Thread2 ───────────► getInstance()

 Reflection ────────► newInstance()

 Serialization ─────► new object
```

**Result:**

- Multiple Registry Instances ❌
- Inconsistent Metrics ❌
- Broken Singleton ❌

---

## 4. Refactoring Strategy

To fix the system we enforced **four key rules**.

### Rule 1 — Private Constructor

Prevents external object creation.

### Rule 2 — Lazy Thread-Safe Initialization

Using **Bill Pugh Holder Pattern**.

### Rule 3 — Block Reflection

Throw exception if constructor called twice.

### Rule 4 — Preserve Singleton During Serialization

Use `readResolve()`.

---

## 5. Architecture After Refactor

```
                getInstance()
                      │
                      ▼
            ┌─────────────────┐
            │ Holder Class     │
            │ static INSTANCE  │
            └─────────┬───────┘
                      │
                      ▼
             ┌─────────────────┐
             │ MetricsRegistry │
             │ private ctor    │
             │ counters map    │
             └─────────────────┘
```

Now **every component receives the same object**.

```
App
Loader
Thread1
Thread2
        │
        ▼
   SAME INSTANCE
```

---

## 6. What We Changed

| Component | Before | After |
|-----------|--------|-------|
| Constructor | Public | Private |
| Instance creation | `new` everywhere | `getInstance()` only |
| Thread safety | Not safe | Holder pattern |
| Reflection | Allowed | Blocked with guard |
| Serialization | Created new instance | `readResolve()` returns singleton |
| Loader | created new registry | uses singleton |

---

## 7. Tests That Validate The Fix

### Concurrency Test

Creates many threads racing on `getInstance()`.

**Expected after fix:**

```
Unique instances seen: 1
```

### Reflection Attack

Attempts to create another instance using reflection.

**Expected after fix:**

```
Exception thrown
Reflection blocked
```

### Serialization Test

Serializes and deserializes the registry.

**Expected after fix:**

```
Same object? true
```

---

## 8. Feynman Explanation (Simple)

Imagine a house with **one notebook** where everyone writes numbers.

**Before:**

```
Mom gets notebook
Dad gets notebook
Brother gets notebook
```

Everyone has **different notebooks**, so numbers don't match.

**After Singleton:**

```
There is ONLY ONE notebook in the house.
Everyone must write in that notebook.
```

No duplicates.

---

## 9. Final Result

The refactored design guarantees:

✅ **Exactly one** `MetricsRegistry` instance  
✅ **Thread-safe** access  
✅ Protection against **reflection**  
✅ Protection against **serialization duplication**  
✅ **Consistent global metrics** across the application

This transforms the registry into a **production-grade Singleton**.

---

## Build & Run

```bash
cd singleton-metrics/src
javac com/example/metrics/*.java
java com.example.metrics.App
```

### Validation Commands

After fixing the implementation, run these checks:

**Concurrency check:**
```bash
java com.example.metrics.ConcurrencyCheck
```

**Reflection attack check:**
```bash
java com.example.metrics.ReflectionAttack
```

**Serialization check:**
```bash
java com.example.metrics.SerializationCheck
```

### Expected Output

All tests should confirm:
- Single instance across all threads
- Reflection attack blocked
- Serialization preserves singleton identity
- Metrics loaded correctly from `metrics.properties`
