package io2019.nfsfinder.data.login

class LoginDataSourceSingleton {
    companion object {
        @Volatile
        private var INSTANCE: LoginDataSourceSingleton? = null
        fun getInstance()  =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: LoginDataSourceSingleton().also {
                    INSTANCE = it
                }
            }
    }

    val loginDataSource: LoginDataSource by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        LoginDataSource()
    }
}