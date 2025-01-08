# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Gemini
# java.lang.IllegalArgumentException:  Cannot find class with name "com.perkedel.htlauncher. enumerations. PageViewStyle" .  Ensure that the serialName for this argument is the default fully qualified name
# https://stackoverflow.com/questions/22399572/java-lang-classnotfoundexception-didnt-find-class-on-path-dexpathlist
-keep class com.perkedel.htlauncher.enumerations.PageViewStyle { *; }
-keep class com.perkedel.htlauncher.enumerations.PageGridType { *; }
-keep class com.perkedel.htlauncher.enumerations.ShowWhichIcon { *; }
-keep class com.perkedel.htlauncher.enumerations.ThirdButtonPosition { *; }
-keep class com.perkedel.htlauncher.enumerations.Screen { *; }
-keep class com.perkedel.htlauncher.enumerations.ItemDetailPaneNavigate { *; }
-keep class com.perkedel.htlauncher.enumerations.ItemExtraPaneNavigate { *; }
-keep class com.perkedel.htlauncher.enumerations.InternalCategories { *; }
-keep class com.perkedel.htlauncher.enumerations.EditWhich { *; }
-keep class com.perkedel.htlauncher.enumerations.ConfigSelected { *; }
-keep class com.perkedel.htlauncher.enumerations.ButtonTypes { *; }
-keep class com.perkedel.htlauncher.enumerations.ActionGoToSystemSetting { *; }
-keep class com.perkedel.htlauncher.enumerations.ActionDataLaunchType { *; }
-keeppackagenames com.perkedel.htlauncher.enumerations