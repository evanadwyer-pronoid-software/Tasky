package com.pronoidsoftware.auth.data.di

import com.pronoidsoftware.auth.data.EmailPatternValidator
import com.pronoidsoftware.auth.domain.PatternValidator
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
