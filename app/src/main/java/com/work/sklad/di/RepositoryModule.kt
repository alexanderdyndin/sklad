package com.work.sklad.di

import com.work.sklad.data.repository.SharedPreferencesRepository
import com.work.sklad.domain.repository.ISharedPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class  RepositoryModule {

    @Binds
    abstract fun bindPreferences(repository: SharedPreferencesRepository): ISharedPreferencesRepository

}