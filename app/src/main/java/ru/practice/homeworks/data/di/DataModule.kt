package ru.practice.homeworks.data.di


import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.practice.homeworks.data.repository.CatDataRepositoryImpl
import ru.practice.homeworks.domain.repository.CatDataRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindCatDataRepositoryImpl(impl: CatDataRepositoryImpl) : CatDataRepository

}