# Immutable Classes (Incident Tickets)

## 1. Problem Context

The CLI tool **HelpLite** manages support tickets.

Each ticket represents an incident report containing fields like:

- ticket id
- reporter email
- title
- description
- priority
- tags
- assignee
- SLA minutes
- source

A ticket should behave like a **record of an event**.

Once created, it should **never change**, because changes can break:

- audit logs
- debugging history
- distributed systems consistency

However, the starter implementation made `IncidentTicket` **mutable**.

---

## 2. Starter Code Problems

The original class allowed tickets to change after creation.

Example fields:

```java
private String id;
private String reporterEmail;
private String title;
```

These fields were **not final**, meaning they could be modified.

The class also exposed **public setters**:

```java
public void setPriority(String priority) { this.priority = priority; }
public void setTags(List<String> tags) { this.tags = tags; }
```

(From the starter implementation)

```
IncidentTicket
```

This allowed the ticket to **mutate** anytime.

### ❌ Problem 1 — Mutable Fields

Example scenario:

```java
IncidentTicket t = service.createTicket(...);
t.setPriority("CRITICAL");
```

Now the original ticket is different from when it was created.

This breaks **auditability**.

### ❌ Problem 2 — Service Mutates Tickets

The service layer modified tickets after creation.

Example:

```java
t.setPriority("MEDIUM");
t.setSource("CLI");
```

(Shown in the service implementation)

```
TicketService
```

Then escalation mutates again:

```java
t.setPriority("CRITICAL");
t.getTags().add("ESCALATED");
```

Now the ticket keeps **changing over time**.

### ❌ Problem 3 — Internal List Leakage

The getter returned the real list:

```java
public List<String> getTags() { return tags; }
```

This allows outside code to do:

```java
ticket.getTags().add("HACKED");
```

Which modifies the ticket from outside.

### ❌ Problem 4 — Validation Was Scattered

Validation was done in multiple places.

Example:

```java
if (reporterEmail == null || !reporterEmail.contains("@"))
```

(Seen in the service layer)

```
TicketService
```

This leads to:

- inconsistent validation
- missed checks
- duplicated logic

---

## 3. Architecture Before Refactor

```
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
```

**Example flow:**

```
createTicket()
    ↓
setPriority()
    ↓
assign()
    ↓
external code modifies tags
```

**Result:**

- Ticket state constantly changes ❌
- Audit trail unreliable ❌
- External code can modify state ❌

---

## 4. Refactoring Strategy

To fix the design we applied **Immutable Object** principles.

### Rule 1 — Make Class Immutable

All fields become `private final`.

### Rule 2 — Remove Setters

No field can change after construction.

### Rule 3 — Use Builder Pattern

Allows constructing complex objects safely.

### Rule 4 — Defensive Copying

Prevent outside modification of collections.

### Rule 5 — Centralize Validation

All validation happens in `Builder.build()`.

---

## 5. Architecture After Refactor

```
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
```

Now the ticket **cannot change**.

If we want an update:

```
oldTicket → builder → newTicket
```

---

## 6. Builder Pattern Design

Instead of many constructors:

```java
new IncidentTicket(id, email, title)
```

We now use:

```java
IncidentTicket ticket =
    IncidentTicket.builder()
        .id("TCK-1001")
        .reporterEmail("user@example.com")
        .title("Payment failure")
        .priority("HIGH")
        .build();
```

**Builder advantages:**

- readable
- flexible
- validation before creation

---

## 7. Defensive Copying for Tags

To prevent external mutation we **copy the list**.

Instead of:

```java
this.tags = tags;
```

We do:

```java
this.tags = Collections.unmodifiableList(new ArrayList<>(tags));
```

Now this **fails**:

```java
ticket.getTags().add("HACKED");
```

The ticket stays safe.

---

## 8. Centralized Validation

All checks happen inside `build()`.

Examples include:

- ticket id format
- email format
- title length
- allowed priorities
- SLA range

Validation helpers are provided in a dedicated class.

```
Validation
```

This ensures **one single validation point**.

---

## 9. Updating Tickets (New Instance)

Because the ticket is immutable, updates create **new objects**.

Example:

```java
IncidentTicket updated =
    oldTicket.toBuilder()
        .priority("CRITICAL")
        .build();
```

Now we have:

```
oldTicket  → unchanged
newTicket  → updated
```

---

## 10. Demonstration

The demo program shows why mutability was dangerous.

Example code:

```java
List<String> tags = t.getTags();
tags.add("HACKED_FROM_OUTSIDE");
```

(Shown in the demo)

```
TryIt
```

After refactor this will **no longer work**, because the list is immutable.

---

## 11. Before vs After Summary

| Feature | Before | After |
|---------|--------|-------|
| Fields | Mutable | `final` |
| Setters | Present | Removed |
| Validation | Scattered | Centralized |
| List exposure | Leaked | Defensive copy |
| Object state | Changes | Immutable |
| Updates | Mutate object | Create new object |

---

## 12. Feynman Explanation (Super Simple)

Imagine writing a **receipt**.

**Before:**

```
Write receipt
Then erase parts
Rewrite values
Erase again
```

The receipt keeps changing.

**After immutability:**

```
Receipt printed.
You cannot change it.
If you need change → print a new receipt.
```

So:

- Old ticket stays the same
- New ticket contains the update

---

## 13. Benefits of Immutability

✅ **Thread-safe** by default  
✅ Easier **debugging**  
✅ Reliable **audit logs**  
✅ Safe for **distributed systems**  
✅ Prevents **accidental mutation**

This is why many important Java classes are immutable:

- `String`
- `Integer`
- `LocalDate`

---

## 14. Final Result

The system now ensures:

✅ tickets **cannot change** after creation  
✅ validation happens **once** during build  
✅ external code **cannot modify** internal state  
✅ updates produce **new ticket instances**

This results in **predictable, safe, and maintainable** ticket objects.

---

## Build & Run

```bash
cd immutable-tickets/src
javac com/example/tickets/*.java TryIt.java
java TryIt
```

### Expected Output

The demo will show:
- Building immutable tickets using the Builder pattern
- Attempting to modify tags (fails safely)
- Creating updated tickets as new instances
- Centralized validation catching invalid inputs
