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

-dontwarn com.pronoidsoftware.core.data.networking.HttpClientExtensionKt
-dontwarn com.pronoidsoftware.core.data.work.DataErrorToWorkerResultKt
-dontwarn com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
-dontwarn com.pronoidsoftware.core.database.entity.ReminderEntity
-dontwarn com.pronoidsoftware.core.database.entity.TaskEntity
-dontwarn com.pronoidsoftware.core.database.entity.sync.CreatedReminderPendingSyncEntity
-dontwarn com.pronoidsoftware.core.database.entity.sync.CreatedTaskPendingSyncEntity
-dontwarn com.pronoidsoftware.core.database.entity.sync.UpdatedReminderPendingSyncEntity
-dontwarn com.pronoidsoftware.core.database.entity.sync.UpdatedTaskPendingSyncEntity
-dontwarn com.pronoidsoftware.core.database.mappers.AgendaMappersKt
-dontwarn java.lang.invoke.StringConcatFactory