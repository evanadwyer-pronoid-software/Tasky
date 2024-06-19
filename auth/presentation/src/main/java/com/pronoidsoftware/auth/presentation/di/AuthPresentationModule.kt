package com.pronoidsoftware.auth.presentation.di

import com.pronoidsoftware.auth.domain.PatternValidator
import com.pronoidsoftware.auth.domain.UserDataValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthPresentationModule {

    @Provides
    @ViewModelScoped
    fun provideUserDataValidator(emailPatternValidator: PatternValidator): UserDataValidator {
        return UserDataValidator(emailPatternValidator)
    }
}
