package com.example.llama

import android.app.ActivityManager
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import com.example.llama.ui.MainApp
import com.example.llama.ui.theme.LlamaAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LlamaAndroidTheme {
                MainApp()
            }
        }
    }
}

//class MainActivity(
//    clipboardManager: ClipboardManager? = null,
//): ComponentActivity() {
//    private val tag: String? = this::class.simpleName
//
//    private val clipboardManager by lazy { clipboardManager ?: getSystemService<ClipboardManager>()!! }
//
//    private val viewModel: MainViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        StrictMode.setVmPolicy(
//            VmPolicy.Builder(StrictMode.getVmPolicy())
//                .detectLeakedClosableObjects()
//                .build()
//        )
//
//        setContent {
//            LlamaAndroidTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MainCompose(
//                        viewModel,
//                        clipboardManager
//                    )
//                }
//
//            }
//        }
//    }
//}

@Composable
fun MainCompose(
    viewModel: MainViewModel,
    clipboard: ClipboardManager
) {
    var loadedModel by remember { mutableStateOf(false) }
    fun setLoadedModel(boolean: Boolean) {
        loadedModel = boolean
    }

    if (loadedModel) {
        Column {
            val scrollState = rememberLazyListState()

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(state = scrollState) {
                    items(viewModel.messages) {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            OutlinedTextField(
                value = viewModel.message,
                onValueChange = { viewModel.updateMessage(it) },
                label = { Text("Message") },
            )
            Row {
                Button({ viewModel.send() }) { Text("Send") }
//                Button({ viewModel.bench(8, 4, 1) }) { Text("Bench") }
                Button({ viewModel.clear() }) { Text("Clear") }
                Button({
                    viewModel.messages.joinToString("\n").let {
                        clipboard.setPrimaryClip(ClipData.newPlainText("", it))
                    }
                }) { Text("Copy") }
            }
        }
    } else {
        Button({
            viewModel.load("/sdcard/Android/data/com.example.llama/files/model.gguf")
            setLoadedModel(true)
        }) { Text("Launch") }
    }
}
