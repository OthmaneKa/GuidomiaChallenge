package com.othmanek.guidomiacars.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.othmanek.guidomiacars.common.TypeConverter
import com.othmanek.guidomiacars.domain.entity.Car

@Database(
    entities = [Car::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class CarDatabase : RoomDatabase() {
    abstract val carDao: CarDao
}