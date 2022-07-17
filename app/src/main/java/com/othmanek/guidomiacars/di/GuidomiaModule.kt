package com.othmanek.guidomiacars.di

import android.content.Context
import android.content.res.Resources
import com.othmanek.guidomiacars.domain.entity.Car
import com.othmanek.guidomiacars.domain.use_case.GetCarsUseCase
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideAppResource(@ApplicationContext context: Context): Resources {
        return context.resources
    }

    @Singleton
    @Provides
    fun provideCarUseCase(
        moshiAdapter: JsonAdapter<List<Car>>,
        resources: Resources
    ): GetCarsUseCase {
        return GetCarsUseCase(moshiAdapter, resources)
    }
}