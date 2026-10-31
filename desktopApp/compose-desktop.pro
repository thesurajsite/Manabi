# Room's bundled SQLite driver binds to its native library by JNI method/class names.
-keep class androidx.sqlite.** { *; }
-keep class * extends androidx.room.RoomDatabase { <init>(); }
