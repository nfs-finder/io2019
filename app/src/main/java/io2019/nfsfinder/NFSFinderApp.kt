package io2019.nfsfinder

import android.app.Application

class NFSFinderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: NFSFinderApp
            private set
    }
}