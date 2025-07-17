package com.example.llama.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.llama.model.ModelManager
import com.example.llama.model.Fields
import java.io.File
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.plus

sealed interface ModelState{
    data class Completion(val inputText: String, val output: String) : ModelState
    object Loading : ModelState
    object Empty : ModelState
}

class SharedViewModel(
    private val modelManager: ModelManager,
    private val applicationContext: Context
) : ViewModel() {

    init {
        val model = File(applicationContext.getExternalFilesDir(null), "model.gguf")
        val modelPath = model.absolutePath
        viewModelScope.launch {
            modelManager.load(modelPath)
        }
    }

    // Private mutable state
    private val _modelState: MutableStateFlow<ModelState> = MutableStateFlow<ModelState>(
        ModelState.Empty
    )

    // Public immutable state exposed
    val modelState: StateFlow<ModelState> = _modelState.asStateFlow()

    fun setState(newState: ModelState) {
        _modelState.value = newState
    }

    suspend fun load(pathToModel: String) {
        modelManager.load(pathToModel)
    }
//
//    fun updateModule(moduleName: String, fieldNamesLiteral: String) {
//        modelManager.updateModule(moduleName, fieldNamesLiteral)
//    }

    fun generateSummary(input: String) {
        setState(ModelState.Loading)
//        var summary: String = ""
//        viewModelScope.launch {
//            modelManager.summarize(input)
//                .collect {
//                    Log.d("Debug", "Flow emitted: $it")
//                    summary += it }
//            Log.d("SharedVM", summary)
//            setState(ModelState.Completion(input, summary))
//        }
        viewModelScope.launch {
            val result: String = modelManager.summarize(input)
            setState(ModelState.Completion(input, result))
        }

    }

}
