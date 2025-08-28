package com.example.hearitbetter.di

import android.content.Context
import com.example.hearitbetter.audioManager.AudioPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideAudioPlayer(@ApplicationContext context: Context) = AudioPlayer(context)
}