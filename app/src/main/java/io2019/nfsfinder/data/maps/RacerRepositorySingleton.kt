package io2019.nfsfinder.data.maps

class RacerRepositorySingleton {
    companion object {
        @Volatile
        private var INSTANCE: RacerRepositorySingleton? = null
        fun getInstance()  =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: RacerRepositorySingleton().also {
                        INSTANCE = it
                    }
            }
    }

    val racerRepository: RacerRepository by lazy {
        RacerRepository()
    }
}