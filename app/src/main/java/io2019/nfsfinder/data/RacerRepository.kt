package io2019.nfsfinder.data


import io2019.nfsfinder.data.database.RequestHandler
import java.util.*
import kotlin.concurrent.fixedRateTimer

class RacerRepository (val loginRepository: LoginRepository) {
    val racerMap: MutableMap<Int, Racer> = HashMap()
    private var refreshTime: Long = 3000 //frequency of updates in milliseconds
    private var searchRadius: Long = 1000 //in meters
    val requestHandler = RequestHandler()

    /*init {
        val updateTask = fixedRateTimer(period = refreshTime) {
            this@RacerRepository.updateRacerMap()
        }
    }*/

    fun updateRacerMap() {
        val updateMap: (Set<Racer>) -> Unit = {
            for (racer in it) {
                if (racerMap.containsKey(racer.userId)) {
                    racerMap.getValue(racer.userId).username = racer.username
                    racerMap.getValue(racer.userId).car = racer.car
                    racerMap.getValue(racer.userId).location = racer.location
                } else {
                    racerMap[racer.userId] = racer
                }
            }
        }

        requestHandler.getRacers(loginRepository.user!!.userId, searchRadius, updateMap)
    }
}