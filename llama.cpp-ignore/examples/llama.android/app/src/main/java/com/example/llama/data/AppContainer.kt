package com.example.llama.data

import android.content.Context
import com.example.llama.model.ModelManager
import java.io.File

interface AppContainer {
    val modelManager: ModelManager
    val sharedViewModel: SharedViewModel
}

class DefaultAppContainer(
    val context: Context
): AppContainer {
//    val model = File(context.getExternalFilesDir(null), "model.task")
//    val modelPath = model.absolutePath
//
//    val moduleName = "Incident Report"
////    val fieldNames = "title, incident_type, date_time, location, cause, issue, resolution"
//    val fieldNames = "date_time, location, issue, resolution"


    override val modelManager: ModelManager by lazy {
        ModelManager()
    }

    override val sharedViewModel: SharedViewModel by lazy {
        SharedViewModel(modelManager, context)
    }

}
