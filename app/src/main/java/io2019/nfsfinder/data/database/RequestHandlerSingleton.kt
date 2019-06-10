package io2019.nfsfinder.data.database

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