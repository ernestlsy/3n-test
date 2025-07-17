package com.example.llama

import android.app.Application
import com.example.llama.data.AppContainer
import com.example.llama.data.DefaultAppContainer

class MainApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}
