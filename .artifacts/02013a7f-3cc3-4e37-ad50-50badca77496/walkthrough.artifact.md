# Walkthrough - Android Multi-Sensor Jetpack Compose App

An Android application has been successfully created and implemented using **Kotlin** and **Jetpack Compose**, integrating multiple hardware sensors from different categories with real-time UI reaction, lifecycle management, and fallback handling.

---

## 💡 What Was Accomplished

### 1. Multi-Sensor Implementation (`MainActivity.kt`)
- **Motion Sensor (`Sensor.TYPE_ACCELEROMETER`)**: Tracks real-time $X, Y, Z$ acceleration values.
- **Environment Sensor (`Sensor.TYPE_LIGHT`)**: Measures ambient lux intensity.
- **Interactive Tilt Visualizer**: A 2D Canvas container where a glowing Material 3 ball rolls smoothly based on device tilt.
- **Dynamic UI Reaction**: Ambient light level dynamically tints the app background and updates brightness condition status (*Dark*, *Normal*, *Sunlight*).
- **Lifecycle Management**: Correctly registers sensor listeners (`SensorManager.registerListener`) on `ON_RESUME` and unregisters them on `ON_PAUSE` using `DisposableEffect` and `LifecycleEventObserver`.
- **Graceful Fallback**: Displays informative warning banners if physical sensors are missing on devices or emulators.

### 2. Comprehensive Documentation (`README.md`)
- Added clear project description, breakdown of sensors used and their constants, real-time UI reaction details, and step-by-step instructions for testing virtual sensors in the Android Emulator Extended Controls.

### 3. Build Verification
- Updated `compileSdk` to `37` to support latest core libraries and successfully verified compilation via Gradle (`BUILD SUCCESSFUL`).

---

## 📸 Demo & UI Preview

```
+-------------------------------------------------------+
|  Multi-Sensor Dashboard                       [AppBar]|
+-------------------------------------------------------+
| 📦 Motion Sensor (Accelerometer)                      |
|    Constants: Sensor.TYPE_ACCELEROMETER               |
|    [  X: 0.12  ]  [  Y: 9.81  ]  [  Z: 0.05  ]         |
|                                                       |
|    Interactive Tilt Visualizer:                       |
|    +-----------------------------------------------+  |
|    |                      (+) [●]                  |  |
|    |         Tilt Phone to Move Ball               |  |
|    +-----------------------------------------------+  |
+-------------------------------------------------------+
| 💡 Environment Sensor (Light)                         |
|    Constants: Sensor.TYPE_LIGHT                       |
|    Ambient Brightness            [ 350.0 lx ]         |
|    Condition: Normal Indoor Lighting 💡               |
|    ============================================       |
+-------------------------------------------------------+
```

---

## 🧪 Verification Results

### Automated Tests & Build
- Ran `./gradlew app:assembleDebug`: **`BUILD SUCCESSFUL`**
