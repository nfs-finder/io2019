package io2019.nfsfinder.data.database

import android.util.Log
import io2019.nfsfinder.data.Racer
import io2019.nfsfinder.data.model.LoggedInUser

class RequestHandlerSingleton {
    companion object {
        @Volatile
        private var INSTANCE: RequestHandlerSingleton? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RequestHandlerSingleton().also {
                    INSTANCE = it
                }
            }
    }

    val requestHandler: RequestHandler by lazy {
        RequestHandler()
    }
}