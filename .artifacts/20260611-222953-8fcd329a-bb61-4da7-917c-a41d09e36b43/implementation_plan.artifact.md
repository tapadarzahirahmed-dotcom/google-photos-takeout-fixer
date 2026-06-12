# Implementation Plan - Google Photos Takeout Fixer

The goal is to build a complete Android application that fixes Google Photos Takeout metadata issues directly on-device using Clean Architecture and modern Android components.

## User Review Required

> [!IMPORTANT]
> The app requires ExifTool and FFmpeg. These are usually native binaries. I will provide wrapper classes that assume these binaries are available (either bundled in assets/jniLibs or provided via a system-wide installation if possible on Android). For this implementation, I will focus on the Kotlin wrappers and process execution logic.

## Proposed Changes

### Project Setup
- `settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`: Configure dependencies for Compose, Hilt, WorkManager, etc.

### Domain Layer
- `models/`: Define `TakeoutFile`, `Metadata`, `Album`, `ScanResult`.
- `repositories/`: Interfaces for file access and metadata processing.
- `usecases/`: Business logic for scanning, matching JSON, and applying fixes.

### Data Layer
- `repositories/`: Implementation using SAF and Room (if needed for large takeouts, but mostly in-memory/streaming).
- `utils/`: `ExifToolManager`, `FFmpegManager`, `JsonParser`.
- `scanner/`: Recursive file scanner for SAF.

### Presentation Layer
- `ui/`: Compose screens for Home, Scan, Analysis, Preview, Processing, and Results.
- `viewmodels/`: Managing UI state and triggering use cases.

### Background Layer
- `workers/`: WorkManager tasks for heavy lifting (Scanning and Fixing).

## Verification Plan

### Automated Tests
- Unit tests for `JsonParser` and matching heuristics.
- `./gradlew test`

### Manual Verification
- Deploy to emulator/device.
- Select a sample Takeout folder (mocked or real).
- Verify scan results and metadata reconstruction.
- Verify timestamp updates.
