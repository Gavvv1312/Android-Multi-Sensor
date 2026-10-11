# Android Multi-Sensor Jetpack Compose Dashboard

A complete, modern Android application built with **Kotlin** and **Jetpack Compose** demonstrating real-time hardware sensor integration across different categories (Motion and Environment sensors) with proper lifecycle management and dynamic UI reactions.

---

## 📱 App Overview & Features

This application showcases live data streaming from device hardware sensors and translates them into reactive UI updates:
1. **Motion Sensor (`Sensor.TYPE_ACCELEROMETER`)**: Tracks device acceleration across the X, Y, and Z axes in real-time and powers an interactive 2D tilt ball visualizer that rolls according to device orientation.
2. **Environment Sensor (`Sensor.TYPE_LIGHT`)**: Measures ambient light intensity in Lux and dynamically alters background tint and status indicators based on environmental lighting (Dark mode / Low light vs. Bright sunlight).
3. **Robust Lifecycle Management**: Utilizes Compose `DisposableEffect` combined with `LifecycleEventObserver` to register sensor listeners in `onResume` and unregister them in `onPause`, preventing battery drain and memory leaks.
4. **Graceful Fallback Handling**: Detects if specific hardware sensors are missing on physical devices or emulators and displays informative warning banners.

---

## 🔬 Sensors Used

| Sensor Name | Category | Android Constant | Description |
| :--- | :--- | :--- | :--- |
| **Accelerometer** | Motion Sensor | `Sensor.TYPE_ACCELEROMETER` | Measures acceleration force in $m/s^2$ applied to the device on all three physical axes ($X, Y, Z$). |
| **Ambient Light** | Environment Sensor | `Sensor.TYPE_LIGHT` | Measures ambient illumination level in lux ($lx$). |

---

## 🎨 Real-Time UI Reactions

- **Accelerometer → Tilt Visualizer**:
  - The X and Y axis values dynamically compute offsets to move a glowing Material 3 styled ball inside a bounded 2D Canvas container. Tilting your phone or emulator makes the ball roll smoothly.
- **Light Sensor → Ambient Brightness & Theme Tint**:
  - The measured lux value updates a progress bar and status label (*Dark / Low Light*, *Normal Indoor Lighting*, *Very Bright / Sunlight*).
  - The background color of the dashboard subtly adapts its tint depending on lighting conditions.

---

## 🚀 How to Run & Test in Android Emulator

Because physical devices or standard emulators may not always have active environmental light or motion inputs by default, you can easily simulate sensor values using the **Android Emulator Extended Controls**:

1. Open **Android Studio** and launch your **Android Virtual Device (AVD)**.
2. Once the app is running on the emulator:
   - Click on the ** három titik (...) / Extended Controls** icon on the emulator sidebar toolbar.
   - Navigate to the **Virtual Sensors** tab (or **Sensors** / **Phone** depending on emulator version).
   - **To test Accelerometer**: 
     - Adjust the 3D rotation sliders or use the virtual device rotation controls. Watch the X, Y, Z badges update instantly and the ball roll inside the tilt visualizer box.
   - **To test Light Sensor**:
     - Under Virtual Sensors / Ambient Light, drag the Lux slider (e.g., from `10 lx` for dark room up to `5000 lx` for bright sunlight). Observe the ambient brightness card and background tint update in real-time.

---

## 🛠️ Code Structure

- **`MainActivity.kt`**: Contains `SensorDashboardScreen`, state collectors (`rememberFloatState`), `SensorEventListener`, lifecycle observers (`DisposableEffect`), and UI composables (`TiltVisualizerBox`, `SensorValueBadge`, `SensorMissingWarning`).
- **`Theme.kt` / `Color.kt` / `Type.kt`**: Material 3 styling and color palette.
