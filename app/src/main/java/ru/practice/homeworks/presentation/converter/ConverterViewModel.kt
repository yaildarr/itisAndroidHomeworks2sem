package ru.practice.homeworks.presentation.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.practice.homeworks.domain.usecase.ConvertUseCase
import ru.practice.homeworks.presentation.converter.ConverterUiState.Idle
import ru.practice.homeworks.presentation.converter.ConverterUiState.Loading
import ru.practice.homeworks.presentation.converter.ConverterUiState.Success
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val useCase: ConvertUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConverterUiState>(Idle)
    val uiState: StateFlow<ConverterUiState> = _uiState.asStateFlow()

    fun convert(from: String, to: String, amount: String) {
        viewModelScope.launch {
            _uiState.value = Loading

            runCatching {
                useCase.invoke(from, to, amount)
            }.onSuccess { data ->
                _uiState.value = Success(data)
            }.onFailure { exception ->
                when (exception) {
                    is IOException -> {
                        _uiState.value = ConverterUiState.Error("Ошибка сети")
                    }
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> _uiState.value = ConverterUiState.Error("Неправильный формат валюты")
                            404 -> _uiState.value = ConverterUiState.Error("Валюта не найдена")
                            500 -> _uiState.value = ConverterUiState.Error("Ошибка сервера")
                            else -> _uiState.value = ConverterUiState.Error("Ошибка ${exception.code()}")
                        }
                    }
                    else -> {
                        _uiState.value = ConverterUiState.Error("Неизвестная ошибка")
                    }
                }
            }

        }
    }

    fun resetState() {
        _uiState.value = Idle
    }
}

sealed class ConverterUiState{
    object Idle: ConverterUiState()
    object Loading : ConverterUiState()
    data class Success(val result: String) : ConverterUiState()
    data class Error(val message: String) : ConverterUiState()
}