package com.othmanek.guidomiacars.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.othmanek.guidomiacars.domain.entity.Car

@Dao
interface CarDao {

    @Query("SELECT * FROM Car")
    fun getAllCars(): List<Car>

    @Insert
    fun insertCar(car: Car)

    @Query("SELECT * FROM Car WHERE model = :model OR make = :make")
    fun getCarsByParameters(model: String, make: String): List<Car>

    @Query("SELECT make from Car")
    fun getAllMakeExisting(): List<String>

    @Query("SELECT model from Car")
    fun getAllModelsExisting(): List<String>
}