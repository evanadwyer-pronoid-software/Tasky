package com.pronoidsoftware.core.data.di

import android.content.Context
import com.pronoidsoftware.core.data.agenda.PngPhotoCompressor
import com.pronoidsoftware.core.domain.agendaitem.PhotoCompressor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotoCompressorModule {

    @Provides
    @Singleton
    @Named("CompressionThresholdSizeBytes")
    fun provideCompressionThresholdSizeBytes(): Long {
        return 1_048_576L // 1 MB in bytes
    }

    @Provides
    @Singleton
    fun providePhotoCompressor(
        @ApplicationContext context: Context,
        @Named("CompressionThresholdSizeBytes") compressionThreshold: Long,
    ): PhotoCompressor {
        return PngPhotoCompressor(context, compressionThreshold)
    }
}
