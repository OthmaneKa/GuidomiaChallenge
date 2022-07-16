package com.othmanek.guidomiacars.di

import com.othmanek.guidomiacars.domain.entity.Car
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GuidomiaModule {

    @Singleton
    @Provides
    fun provideMoshiAdapterForCar(): JsonAdapter<List<Car>> {
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Car::class.java)
        return moshi.adapter(listType)
    }
}