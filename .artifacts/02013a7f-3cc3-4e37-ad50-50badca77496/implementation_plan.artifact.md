# Implementation Plan - Android Multi-Sensor Jetpack Compose App

This plan outlines the creation of a complete Android application using Kotlin and Jetpack Compose that utilizes two distinct hardware sensors (`Sensor.TYPE_ACCELEROMETER` and `Sensor.TYPE_LIGHT`) with real-time UI reaction, proper lifecycle management, and fallback handling.

## User Review Required

> [!IMPORTANT]
> - The app will use `Sensor.TYPE_ACCELEROMETER` (Motion Sensor) to control an interactive ball/indicator on a 2D canvas/box responding to device tilt.
> - The app will use `Sensor.TYPE_LIGHT` (Environment Sensor) to measure ambient light (Lux) and dynamically adjust UI elements (such as theme adaptation or ambient brightness card styling).
> - Proper lifecycle management will be handled via Jetpack Compose `DisposableEffect` and `LifecycleEventObserver` to register/unregister listeners during `onResume`/`onPause`.

## Open Questions

- None. The requirements are fully specified.

## Proposed Changes

### [Component Name]

#### [MODIFY] [MainActivity.kt](file:///C:/Users/advan pixwar/AndroidStudioProjects/androideyy/app/src/main/java/com/example/androideyy/MainActivity.kt)
- Implement state management for Accelerometer (`X`, `Y`, `Z`) and Light Sensor (`Lux`).
- Add sensor lifecycle management (`SensorManager`, `SensorEventListener`, `DisposableEffect`, `LifecycleEventObserver`).
- Build a polished Material 3 Jetpack Compose UI featuring:
  - Header / App Title describing active sensors.
  - Interactive Tilt Ball visualization driven by Accelerometer values with smooth scaling/clamping.
  - Ambient Light visualizer card with dynamic theme adaptation or lighting feedback (Lux value, dark/light visual cues).
  - Fallback / error banners if physical sensors are missing on the device/emulator.

#### [NEW] [README.md](file:///C:/Users/advan pixwar/AndroidStudioProjects/androideyy/README.md)
- Title and short description of the Sensor Demo App.
- Detailed breakdown of sensors used (`Sensor.TYPE_ACCELEROMETER` and `Sensor.TYPE_LIGHT`), categories, and constants.
- Explanation of real-time UI reactions.
- Step-by-step guide on how to run and test sensors in the Android Emulator using Extended Controls (Virtual Sensors).

## Verification Plan

### Automated Tests
- Build verification via `./gradlew app:assembleDebug` to ensure compilation and no syntax errors.

### Manual Verification
- Instructions for the user to deploy to an emulator or physical device and test sensor sliders in Emulator Extended Controls -> Virtual Sensors.
