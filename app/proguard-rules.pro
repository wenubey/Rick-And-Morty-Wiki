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

-dontwarn org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
-dontwarn org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
-dontwarn org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
-dontwarn org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
-dontwarn org.slf4j.impl.StaticLoggerBinder
-assumenosideeffects class timber.log.Timber {
    public static *** v(...);
    public static *** d(...);
}
-assumenosideeffects class org.slf4j.Logger {
    public *** trace(...);
    public *** debug(...);
}
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
}