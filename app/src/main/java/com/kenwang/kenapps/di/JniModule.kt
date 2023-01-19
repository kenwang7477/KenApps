package com.kenwang.kenapps.di

import com.kenwang.kenapps.jnilib.KenNativeLib
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class JniModule {

    @Provides
    @Singleton
    fun provideKenNativeLib() = KenNativeLib()
}
