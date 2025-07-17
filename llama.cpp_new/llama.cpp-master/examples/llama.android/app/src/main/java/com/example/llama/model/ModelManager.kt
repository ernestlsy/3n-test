package com.example.llama.model

import android.llama.cpp.LLamaAndroid
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.llama.utils.Formatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.plus

class ModelManager(
    val llamaAndroid: LLamaAndroid = LLamaAndroid.instance()
) {
    val module: Module = Module("incident report", "title, incident_type, date_time, location, cause, issue, resolution")

    suspend fun load(path: String) {
        try {
            llamaAndroid.load(path)
            Log.d("ModelManager", "Loaded $path")
        } catch (exc: IllegalStateException) {
            Log.e("ModelManager", "load() failed", exc)
        }
    }

    suspend fun summarize(input: String): String = withContext(Dispatchers.IO) {
        val formattedInput = Formatter.formatInput(module, input)
        val resultFlow: Flow<String> = llamaAndroid.send(formattedInput)
        val outputBuilder = StringBuilder()

        resultFlow.collect { token ->
            outputBuilder.append(token)

            // Hard coded check for end of first json string to save time
            if (token.contains("}")) return@collect
        }

        outputBuilder.toString()
    }
}
