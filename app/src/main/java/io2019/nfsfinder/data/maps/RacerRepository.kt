package io2019.nfsfinder.data.maps

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import io2019.nfsfinder.data.database.RequestHandler
import io2019.nfsfinder.data.login.LoginRepository
import io2019.nfsfinder.data.login.LoginRepositorySingleton
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.properties.Delegates

class RacerRepository {
    val loginRepository: LoginRepository = LoginRepositorySingleton.getInstance().loginRepository
    val racerMap: MutableMap<Int, Racer> = HashMap()
    private var refreshTime: Long = 3000 //frequency of updates in milliseconds
    private var searchRadius: Long = 2000 //in meters
    val requestHandler = RequestHandler()
    val LOGTAG = "RacerRepository"

    var updated: Boolean by Delegates.observable(false) {_, _, _ ->
        afterUpdate?.invoke()
    }

    var afterUpdate: (() -> Unit)? = null

    lateinit var currentLocation: LatLng

    init {
        val updateMapTask = fixedRateTimer(period = refreshTime) {
            this@RacerRepository.updateRacerMap()
        }

        val updateLocTask = fixedRateTimer(period = refreshTime) {
            this@RacerRepository.updateLocation()
        }

        Log.d(LOGTAG, "Initialized cyclic tasks")
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

    fun updateLocation() {
        val errorReaction: (Exception) -> Unit = {
            throw it
        }

        if (::currentLocation.isInitialized) {
            val lat = currentLocation.latitude
            val lng = currentLocation.longitude

            requestHandler.updateLoc(loginRepository.user!!.userId, lat, lng, errorReaction)
        }
    }
}