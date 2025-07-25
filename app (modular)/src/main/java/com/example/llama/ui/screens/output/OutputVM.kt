package com.example.llama.ui.screens.output

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.llama.MainApplication
import com.example.llama.data.ModelState
import com.example.llama.data.SharedViewModel
import com.example.llama.model.Fields
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface OutputState{
    data class Editing(val inputText: String, val output: String) : OutputState
    object Loading : OutputState
    object Empty : OutputState
}

class OutputViewModel(
    private val sharedViewModel: SharedViewModel
) : ViewModel() {
    private val modelState: StateFlow<ModelState> = sharedViewModel.modelState

    // Private mutable state
    private val _uiState: MutableStateFlow<OutputState> = MutableStateFlow<OutputState>(
        OutputState.Empty
    )

    // Public immutable state exposed
    val uiState: StateFlow<OutputState> = _uiState.asStateFlow()

    // State setter
    fun setState(newState: OutputState) {
        _uiState.value = newState
    }

    init {
        viewModelScope.launch {
            modelState.collect { state ->
                val newState = when (state) {
                    is ModelState.Completion -> OutputState.Editing(state.inputText, state.output)
                    is ModelState.Loading -> OutputState.Loading
                    is ModelState.Empty -> OutputState.Empty
                }
                setState(newState)
            }
        }
    }

//    fun updateFields(fields: Fields) {
//        if (uiState.value is OutputState.Editing) {
//            setState(OutputState.Editing((uiState.value as OutputState.Editing).inputText, fields))
//        } else {
//            Log.d("OutputVM", "Invalid state")
//        }
//    }

//    fun submitFields() {
//        if (uiState.value is OutputState.Editing) {
//            Log.d("OutputVM", "Submitting fields:")
//            val output: OutputState.Editing = (uiState.value as OutputState.Editing)
//            val fields: Fields = output.fields
//            fields.values.forEachIndexed { index, value ->
//                Log.d("OutputVM", "${fields.fieldNames[index]}: $value")
//            }
//
//        } else {
//            Log.d("OutputVM", "Invalid state")
//        }
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MainApplication)
                val sharedViewModel = application.container.sharedViewModel
                OutputViewModel(sharedViewModel)
            }
        }
    }
}
