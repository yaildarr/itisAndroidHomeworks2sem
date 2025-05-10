package ru.practice.homeworks.presentation.cats

import android.app.Application
import android.content.Context
import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.annotation.UiContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practice.homeworks.R
import ru.practice.homeworks.domain.model.CatDataModel
import ru.practice.homeworks.domain.usecase.CatByIdUseCase
import ru.practice.homeworks.domain.usecase.RandomCatUseCase
import ru.practice.homeworks.domain.wrapper.CatWrapper

import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val randomCatUseCase : RandomCatUseCase,
    val catByIdUseCase: CatByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<CatDataModel>>(UiState.Loading)
    val uiState: StateFlow<UiState<CatDataModel>> = _uiState

    private val _uiStateId = MutableStateFlow<UiState<CatWrapper>>(UiState.Loading)
    val uiStateId: StateFlow<UiState<CatWrapper>> = _uiStateId

    fun loadRandomCat(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val result = runCatching {
                randomCatUseCase.invoke()
            }
            _uiState.value = result.fold(
                onSuccess = { cat ->
                    UiState.Success(cat)
                },
                onFailure = { exception ->
                    when (exception) {
                        is IOException -> {
                            Log.d("MyLog","io")
                            UiState.Error("Ошибка сети")}
                        else -> UiState.Error("Неизвестная ошибка")
                    }
                }
            )
        }
    }

    fun loadListCatById(id: String){
        viewModelScope.launch {
            _uiStateId.value = UiState.Loading

            val result = runCatching {
                catByIdUseCase.invoke(id)
            }
            _uiStateId.value = result.fold(
                onSuccess = { cat ->
                    UiState.Success(cat)
                },
                onFailure = { exception ->
                    when (exception) {
                        is IOException -> {
                            UiState.Error("Ошибка сети")}
                        else -> UiState.Error("Неизвестная ошибка")
                    }
                }
            )
        }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<out T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}