package io2019.nfsfinder.data

import android.util.Log
import io2019.nfsfinder.data.database.RequestHandler
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.properties.Delegates

class RacerRepository (val loginRepository: LoginRepository) {
    val racerMap: MutableMap<Int, Racer> = HashMap()
    private var refreshTime: Long = 3000 //frequency of updates in milliseconds
    private var searchRadius: Long = 2000 //in meters
    val requestHandler = RequestHandler()
    val LOGTAG = "RacerRepository"

    var updated: Boolean by Delegates.observable(false) {_, _, _ ->
        afterUpdate?.invoke()
    }

    var afterUpdate: (() -> Unit)? = null

    /*init {
        val updateTask = fixedRateTimer(period = refreshTime) {
            this@RacerRepository.updateRacerMap()
        }
    }*/

    companion object {
        @Volatile
        private var INSTANCE: RacerRepository? = null
        fun getInstance(loginRepository: LoginRepository) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: RacerRepository(LoginRepository.getInstance(LoginDataSource(RequestHandler()))).also {
                        INSTANCE = it
                    }
                }
    }

    fun updateRacerMap() {
        val updateMap: (Set<Racer>) -> Unit = {
            racerMap.clear()

            for (racer in it) {
                Log.d(LOGTAG, "Adding racer")
                racerMap[racer.userId] = racer
            }

            updated = true
        }

        val errorReaction: (Exception) -> Unit = {
            throw it
        }

        requestHandler.getRacers(loginRepository.user!!.userId, searchRadius, updateMap, errorReaction)
    }
}