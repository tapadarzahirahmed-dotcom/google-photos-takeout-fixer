package com.example.takeoutfixer.di

import com.example.takeoutfixer.data.repository.TakeoutRepositoryImpl
import com.example.takeoutfixer.domain.repository.TakeoutRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTakeoutRepository(
        takeoutRepositoryImpl: TakeoutRepositoryImpl
    ): TakeoutRepository
}
