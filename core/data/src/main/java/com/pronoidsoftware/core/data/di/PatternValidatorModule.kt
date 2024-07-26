package com.pronoidsoftware.core.data.di

import com.pronoidsoftware.core.data.validation.EmailPatternValidator
import com.pronoidsoftware.core.domain.validation.PatternValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PatternValidatorModule {

    @Binds
    @Singleton
    abstract fun bindEmailPatternValidator(
        emailPatternValidator: EmailPatternValidator,
    ): PatternValidator
}
