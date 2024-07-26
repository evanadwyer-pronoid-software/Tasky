package com.pronoidsoftware.core.presentation.ui.di

import com.pronoidsoftware.core.domain.validation.PatternValidator
import com.pronoidsoftware.core.domain.validation.UserDataValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UserDataValidatorModule {

    @Provides
    @ViewModelScoped
    fun provideUserDataValidator(emailPatternValidator: PatternValidator): UserDataValidator {
        return UserDataValidator(emailPatternValidator)
    }
}
