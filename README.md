# Google Photos Takeout Fixer

A lightweight, fast, and secure Android utility to restore metadata (Date Taken, GPS, Descriptions) to photos and videos exported via Google Takeout.

## 🌟 Features
- **Parallel Scanning:** High-speed $O(N)$ scanning using Coroutines to handle thousands of files instantly.
- **Robust JSON Matching:** Handles Google Takeout's inconsistent naming, suffixes, and filename truncations.
- **Comprehensive Metadata Fixes:**
  - **Images:** Restores Date Taken, GPS (including Altitude), and Captions/Descriptions using `androidx.exifinterface`.
  - **Videos:** Restores Creation Time and Descriptions using `FFmpeg` without re-encoding (no quality loss).
- **Safety First:** Replicates original folder structures in a dedicated output directory to keep your originals untouched.
- **Material 3 UI:** Clean, responsive interface with Dark Mode support and dynamic coloring.
- **Social Connectivity:** Integrated links to LinkedIn and GitHub directly in the app.
- **Real-time Progress:** System notifications and in-app progress tracking.

## 🚀 How to Use
1. **Unzip:** Extract your Google Photos Takeout zip files into a folder.
2. **Select Takeout Folder:** Open the app and select the unzipped folder.
3. **Select Output Folder:** Choose a destination folder for your fixed files (recommended).
4. **Scan:** Click "Start Scan" to identify matches.
5. **Fix:** Review the analysis and click "Apply Fixes."

## 🛠️ Technical Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** Clean Architecture + MVVM + Hilt (DI)
- **Metadata Engine:** FFmpegKit (16KB page support), AndroidX ExifInterface
- **Concurrency:** Kotlin Coroutines & Flow

## 🤝 Support the Project
This app is free to use. If it has made your day a little easier, you can support future development:
- **Razorpay:** [Pay via Razorpay](https://razorpay.me/@zahir)
- **UPI:** `zahirahmedt-1@okicici`

## 👤 Created By
**Zahir**
- [LinkedIn](https://www.linkedin.com/in/zahir49)
- [GitHub](https://github.com/tapadarzahirahmed-dotcom)

---
*Disclaimer: This app is not affiliated with Google. Always keep a backup of your original photos before processing.*
