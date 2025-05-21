package ru.practice.homeworks.presentation.chart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.viewModelScope


class ChartViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ChartUiState>(ChartUiState.Idle)
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()


    fun validateAndDrawGraph(pointsCount: String, valuesInput: String) {
        viewModelScope.launch {
            try {
                val pointsCountInt = pointsCount.toIntOrNull()
                if (pointsCountInt == null || pointsCountInt <= 0) {
                    _uiState.value = ChartUiState.Error("Количество точек должно быть положительным числом")
                    return@launch
                }

                val values = valuesInput.split(",").map { it.trim() }
                if (values.size != pointsCountInt) {
                    _uiState.value = ChartUiState.Error("Количество значений должно совпадать с количеством точек")
                    return@launch

                }

                val floatValues = values.map { it.toFloatOrNull() }
                if (floatValues.any { it == null }) {
                    _uiState.value = ChartUiState.Error("Все значения должны быть числами")
                    return@launch
                }

                val data = floatValues.mapIndexed { index, value ->
                    Pair(index.toFloat(), value!!)
                }

                _uiState.value = ChartUiState.Success(data)
            } catch (e: Exception) {
                _uiState.value = ChartUiState.Error("Неизвестная ошибка")
                return@launch
            }
        }
    }
}


sealed class ChartUiState{
    object Idle: ChartUiState()
    data class Success(val result: List<Pair<Float, Float>>) : ChartUiState()
    data class Error(val message: String) : ChartUiState()
}