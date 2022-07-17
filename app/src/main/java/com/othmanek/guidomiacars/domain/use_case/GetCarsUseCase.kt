package com.othmanek.guidomiacars.domain.use_case

import android.content.res.Resources
import com.othmanek.guidomiacars.R
import com.othmanek.guidomiacars.domain.entity.Car
import com.squareup.moshi.JsonAdapter
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val mAdapter: JsonAdapter<List<Car>>,
    private val resources: Resources
) {
    fun getCarsFromLocalJson(): List<Car> {
        val jsonFile =
            resources.openRawResource(R.raw.car_list).bufferedReader().use { it.readText() }
        val data = mAdapter.fromJson(jsonFile) ?: emptyList()
        data.map {
            it.setImage()
            if (data.indexOf(it) == 0) it.isExpanded = true
        }
        return data
    }
}