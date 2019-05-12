package io2019.nfsfinder.data

import io2019.nfsfinder.data.database.RequestHandler
import java.util.*
import kotlin.concurrent.fixedRateTimer

class RacerRepository (val loginRepository: LoginRepository) {
    val racerMap: MutableMap<Int, Racer> = HashMap()
    private var refreshTime: Long = 3000 //frequency of updates in milliseconds
    private var searchRadius: Long = 1000 //in meters
    val requestHandler = RequestHandler()

    init {
        val updateTask = fixedRateTimer(period = refreshTime) {
            this@RacerRepository.updateRacerMap()
        }
    }

    fun updateRacerMap() {
        val updateMap: (Set<Racer>) -> Unit = {
            racerMap.clear()

            for (racer in it)
                racerMap[racer.userId] = racer
        }

        val errorReaction: (Exception) -> Unit = {
            throw it
        }
        requestHandler.getRacers(loginRepository.user!!.userId, searchRadius, updateMap, errorReaction)
    }
}