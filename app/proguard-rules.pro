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

-dontwarn com.pronoidsoftware.agenda.presentation.detail.AgendaDetailScreenKt
-dontwarn com.pronoidsoftware.agenda.presentation.detail.AgendaDetailViewModel
-dontwarn com.pronoidsoftware.agenda.presentation.detail.AgendaDetailViewModel_HiltModules$KeyModule
-dontwarn com.pronoidsoftware.agenda.presentation.di.ClockModule_ProvideClockFactory
-dontwarn com.pronoidsoftware.agenda.presentation.overview.AgendaOverviewScreenKt
-dontwarn com.pronoidsoftware.agenda.presentation.overview.AgendaOverviewViewModel
-dontwarn com.pronoidsoftware.agenda.presentation.overview.AgendaOverviewViewModel_HiltModules$KeyModule
-dontwarn com.pronoidsoftware.auth.data.AuthRepositoryImplementation
-dontwarn com.pronoidsoftware.auth.presentation.login.LoginScreenKt
-dontwarn com.pronoidsoftware.auth.presentation.login.LoginViewModel
-dontwarn com.pronoidsoftware.auth.presentation.login.LoginViewModel_HiltModules$KeyModule
-dontwarn com.pronoidsoftware.auth.presentation.register.RegisterScreenKt
-dontwarn com.pronoidsoftware.auth.presentation.register.RegisterViewModel
-dontwarn com.pronoidsoftware.auth.presentation.register.RegisterViewModel_HiltModules$KeyModule
-dontwarn com.pronoidsoftware.core.data.auth.EncryptedSessionStorage
-dontwarn com.pronoidsoftware.core.data.di.CoreDataModule_ProvideCIOEngineFactory
-dontwarn com.pronoidsoftware.core.data.di.CoreDataModule_ProvideHttpClientFactory
-dontwarn com.pronoidsoftware.core.data.di.CoreDataModule_ProvideSharedPreferencesFactory
-dontwarn com.pronoidsoftware.core.data.di.DispatchersModule_BindDispatcherProviderFactory
-dontwarn com.pronoidsoftware.core.data.validation.EmailPatternValidator
-dontwarn com.pronoidsoftware.core.presentation.designsystem.LocalClockKt
-dontwarn com.pronoidsoftware.core.presentation.designsystem.ThemeKt
-dontwarn com.pronoidsoftware.core.presentation.ui.di.UserDataValidatorModule_ProvideUserDataValidatorFactory
-dontwarn com.pronoidsoftware.agenda.network.KtorRemoteReminderDataSource
-dontwarn com.pronoidsoftware.core.data.agenda.OfflineFirstReminderRepository
-dontwarn com.pronoidsoftware.core.database.RoomLocalReminderDataSource
-dontwarn com.pronoidsoftware.core.database.dao.ReminderDao
-dontwarn com.pronoidsoftware.core.database.di.DatabaseModule_ProvideReminderDaoFactory
-dontwarn com.pronoidsoftware.core.database.di.DatabaseModule_ProvideReminderDatabaseFactory