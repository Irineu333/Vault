package com.neo.vault

import android.app.Application


lateinit var app: App

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
    }
}