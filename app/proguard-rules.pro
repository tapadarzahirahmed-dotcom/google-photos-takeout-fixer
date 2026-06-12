# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\zahir\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.

# For Hilt
-keep public class * extends android.app.Service
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.view.View
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider

-keep class com.google.gson.** { *; }
-keep class com.example.takeoutfixer.data.models.** { *; }
