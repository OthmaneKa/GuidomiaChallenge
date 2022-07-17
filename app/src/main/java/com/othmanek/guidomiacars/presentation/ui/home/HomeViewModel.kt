package com.othmanek.guidomiacars.presentation.ui.home

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.othmanek.guidomiacars.R
import com.othmanek.guidomiacars.common.Resource
import com.othmanek.guidomiacars.data.local.CarDao
import com.othmanek.guidomiacars.domain.entity.Car
import com.othmanek.guidomiacars.domain.entity.FilterValue
import com.squareup.moshi.JsonAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mAdapter: JsonAdapter<List<Car>>,
    private val resources: Resources,
    private val dao: CarDao
) : ViewModel() {

    private val filterValue = MutableStateFlow<FilterValue>(FilterValue())

    var makeFilterValues = MutableStateFlow<List<String>>(emptyList())
    var modelFilterValues = MutableStateFlow<List<String>>(emptyList())

    init {
        getAllCars()
        updateModelsFilterValues()
        updateMakeFilterValues()
    }

    fun getFilteredList(): Flow<Resource<List<Car>>> = flow {
        emit(Resource.Loading())
        if (filterValue.value.makeValue.isEmpty() && filterValue.value.modelValue.isEmpty()) {
            val data = dao.getAllCars()
            data.map { it.setImage() }
            emit(Resource.Success(data))
        } else {
            val data =
                dao.getCarsByParameters(filterValue.value.modelValue, filterValue.value.makeValue)
            data.map { it.setImage() }
            emit(Resource.Success(data))
        }
    }

    private fun getAllCars(): List<Car> {
        val listOfCars = dao.getAllCars()
        if (listOfCars.isEmpty()) {
            val localFile = getCarsFromLocalJson()
            localFile.forEach { dao.insertCar(it) }
        }
        return dao.getAllCars()
    }

    private fun getCarsFromLocalJson(): List<Car> {
        val jsonFile =
            resources.openRawResource(R.raw.car_list).bufferedReader().use { it.readText() }
        val data = mAdapter.fromJson(jsonFile) ?: emptyList()
        data.map {
            it.setImage()
            if (data.indexOf(it) == 0) it.expanded = true
        }
        return data
    }

    private fun updateMakeFilterValues() {
        val data = dao.getAllMakeExisting()
        val currentList = data.toMutableList()
        currentList.add(0, "Any Make")
        makeFilterValues.value = currentList.toList()
    }

    private fun updateModelsFilterValues() {
        val data = dao.getAllModelsExisting()
        val currentList = data.toMutableList()
        currentList.add(0, "Any Model")
        modelFilterValues.value = currentList.toList()
    }

    fun updateFilterValue(filter: FilterValue) {
        filterValue.value = filter
        getFilteredList()
    }
}