package com.myhabbit.app.di

import com.myhabbit.app.domain.service.PeriodCalculator
import com.myhabbit.app.domain.service.PeriodCalculatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePeriodCalculator(): PeriodCalculator = PeriodCalculatorImpl()
}
