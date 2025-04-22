package com.swallaby.foodon.core.di

import com.swallaby.foodon.data.user.remote.repository.UserRemoteRepositoryImpl
import com.swallaby.foodon.domain.user.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRemoteRepository(
        userRepositoryImpl: UserRemoteRepositoryImpl
    ): UserRepository
}