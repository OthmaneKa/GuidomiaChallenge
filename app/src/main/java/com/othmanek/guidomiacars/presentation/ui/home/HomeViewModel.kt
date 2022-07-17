package com.othmanek.guidomiacars.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.othmanek.guidomiacars.common.Resource
import com.othmanek.guidomiacars.domain.entity.Car
import com.othmanek.guidomiacars.domain.use_case.GetCarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetCarsUseCase
) : ViewModel() {

    private var _data: Flow<Resource<List<Car>>> = emptyFlow()
    val data = _data

    private val _makeValue: MutableStateFlow<String> = MutableStateFlow("")
    val makeValue: Flow<String>
        get() = _makeValue

    private val _modelValue: MutableStateFlow<String> = MutableStateFlow("")
    val modelValue: Flow<String>
        get() = _modelValue

    init {
        getCars()
    }

    fun getCars(): Flow<Resource<List<Car>>> = flow {
        try {
            emit(Resource.Loading())
            val myData = useCase.getCarsFromLocalJson()
            if (_modelValue.value.isEmpty() && _makeValue.value.isEmpty()) {
                emit(Resource.Success(myData))
            } else if (_modelValue.value.isNotEmpty() && _makeValue.value.isEmpty()) {
                val filteredData = myData.filter { it.model == _modelValue.value }
                emit(Resource.Success(filteredData))
            } else if (_modelValue.value.isEmpty() && _makeValue.value.isNotEmpty()) {
                val filteredData = myData.filter { it.make == _makeValue.value }
                emit(Resource.Success(filteredData))
            } else
                emit(Resource.Success(emptyList()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    fun setMakeFilters(data: String) {
        _makeValue.value = data
        getCars()
    }

    fun setModelFilters(data: String) {
        _modelValue.value = data
        getCars()
    }
}