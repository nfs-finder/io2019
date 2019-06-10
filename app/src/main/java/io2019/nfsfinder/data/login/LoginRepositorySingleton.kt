package io2019.nfsfinder.data.login

class LoginRepositorySingleton {
    companion object {
        @Volatile
        private var INSTANCE: LoginRepositorySingleton? = null
        fun getInstance()  =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: LoginRepositorySingleton().also {
                        INSTANCE = it
                    }
            }
    }

    val loginRepository: LoginRepository by lazy {
        LoginRepository()
    }
}