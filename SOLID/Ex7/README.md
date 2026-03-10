# 📘 EX7 — ISP: Smart Classroom Devices

---

## 1️⃣ Problem Context

We are building a **smart classroom controller**.

### The classroom contains multiple devices:

- 📽️ **Projector**
- 💡 **Lights Panel**
- ❄️ **Air Conditioner**
- 📋 **Attendance Scanner**

### The controller can:

- 🔌 Turn devices ON/OFF
- 🔆 Adjust brightness
- 🌡️ Adjust temperature
- ✅ Scan attendance
- 🔌 Connect HDMI input

### Expected output:

```
=== Smart Classroom ===
Projector ON (HDMI-1)
Lights set to 60%
AC set to 24C
Attendance scanned: present=3
Shutdown sequence:
Projector OFF
Lights OFF
AC OFF
```

---

## 2️⃣ What Was Wrong (Design Smell)

All devices implemented one **large interface:**

```java
SmartClassroomDevice
```

### Interface methods:

- `powerOn()`
- `powerOff()`
- `setBrightness()`
- `setTemperatureC()`
- `scanAttendance()`
- `connectInput()`

> ⚠️ **This forced every device to implement all methods.**

---

## 3️⃣ Why This Is Bad

**Many devices do not support most methods.**

### Example:

**AirConditioner**
- `setTemperatureC` → ✅ valid
- `scanAttendance` → ❌ irrelevant
- `connectInput` → ❌ irrelevant

**AttendanceScanner**
- `scanAttendance` → ✅ valid
- `setBrightness` → ❌ irrelevant
- `setTemperatureC` → ❌ irrelevant

**So classes implemented dummy methods:**

```java
// irrelevant
return 0;
```

or

```java
/* do nothing */
```

> ❌ **That is misleading and dangerous.**

---

## 4️⃣ What ISP Means

### Interface Segregation Principle

> **Clients should not be forced to depend on methods they do not use.**

**Instead of one large interface, create small capability interfaces.**

---

## 5️⃣ Before Architecture

### ❌ Fat Interface Design

```
                SmartClassroomDevice
       ----------------------------------------
       |        |        |       |      |      |
     powerOn  powerOff brightness temp scan input
       |
--------------------------------------------------------
|          |            |             |                |
Projector  LightsPanel  AirConditioner  AttendanceScanner
```

**Every device must implement everything.**

### Problems:

- ❌ Dummy methods
- ❌ Confusing design
- ❌ Hidden bugs

---

## 6️⃣ Refactoring Strategy

We split the large interface into **capability interfaces**.

### Examples:

| Capability            | Interface                  |
|-----------------------|----------------------------|
| Power control         | `PowerControl`             |
| Brightness control    | `BrightnessControl`        |
| Temperature control   | `TemperatureControl`       |
| Input connection      | `InputConnectable`         |
| Attendance scanning   | `AttendanceScannerDevice`  |

---

## 7️⃣ After Architecture

### ✅ Capability-Based Interfaces

```
                  PowerControl
                /      |       \
               /       |        \
          Projector  LightsPanel  AirConditioner

           BrightnessControl
                |
           LightsPanel

           TemperatureControl
                |
           AirConditioner

           InputConnectable
                |
           Projector

         AttendanceScannerDevice
                |
         AttendanceScanner
```

**Now each device implements only what it supports.**

---

## 8️⃣ Controller Behavior After Refactoring

The controller now works with **specific capabilities**.

### Example:

- **Projector** → `PowerControl` + `InputConnectable`
- **Lights** → `BrightnessControl` + `PowerControl`
- **AC** → `TemperatureControl` + `PowerControl`
- **Scanner** → `AttendanceScannerDevice`

**Controller calls only relevant methods.**

---

## 9️⃣ Why This Design Is Better

### No dummy methods

Classes only implement **what they need**.

### Clear device capabilities

Each interface describes **one capability**.

### Easy to add new devices

**Example:**

`SmartBoard`

Can implement only:
- `PowerControl`
- `InputConnectable`

**No irrelevant methods required.**

---

## 🔟 Feynman Explanation (Explain Like a Kid)

### Imagine giving every device in your house the same remote control.

**The remote has buttons:**

- 🔌 Power
- 🔆 Brightness
- 🌡️ Temperature
- 📺 HDMI
- 📋 Scan Attendance

**But:**

- ❌ AC doesn't need HDMI
- ❌ Lights don't need temperature
- ❌ Scanner doesn't need brightness

**So many buttons do nothing.**

---

**Better idea:**

> Give each device only the buttons it needs.

**That's ISP.**

---

## 1️⃣1️⃣ Key Takeaways

| Before                           | After                              |
|----------------------------------|------------------------------------|
| One giant interface              | Small capability interfaces        |
| Devices implement useless methods| Devices implement only what they need |

---

## 1️⃣2️⃣ What We Achieved

- ✔️ No dummy methods
- ✔️ Clear device capabilities
- ✔️ Flexible system design
- ✔️ Easier extension for new devices

---

## 🎤 Stage Summary

> **We split a monolithic device interface into capability-based interfaces so that each device implements only the operations it truly supports.**

---
