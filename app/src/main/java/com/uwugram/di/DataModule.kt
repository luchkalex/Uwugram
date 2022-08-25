package com.uwugram.di

import com.uwugram.data.UserStorage
import com.uwugram.data.repositories.UserRepositoryImpl
import com.uwugram.data.storages.FirebaseUserStorage
import com.uwugram.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun provideUserRepository(userStorage: UserStorage): UserRepository {
        return UserRepositoryImpl(userStorage)
    }

    @Provides
    @Singleton
    fun provideUserStorage(): UserStorage {
        return FirebaseUserStorage()
    }
}