package com.kenwang.kenapps.di

import android.content.Context
import com.kenwang.kenapps.data.player.TextToSpeechPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideTextToSpeechPlayer(
        @ApplicationContext context: Context
    ) = TextToSpeechPlayer(context)
}
