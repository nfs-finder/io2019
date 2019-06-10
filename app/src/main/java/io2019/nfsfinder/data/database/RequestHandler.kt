package io2019.nfsfinder.data.database

import android.util.Log
import io2019.nfsfinder.data.maps.Racer
import io2019.nfsfinder.data.login.LoggedInUser


class RequestHandler {
    val LOGTAG = "RequestHandler"

    fun requestLogin (email: String, password: String, validReaction: (LoggedInUser) -> Unit,
                      errorReaction: (Exception) -> Unit) {
        val url = "http://nfsfinder.cba.pl/api/user/login.php"
        val loginTask = LoginRequestAsyncTask(email, password, url, validReaction, errorReaction)

        Log.d(LOGTAG, "Starting login task")

        loginTask.execute()
    }

    fun getRacers (id: Int, radius: Long, validReaction: (Set<Racer>) -> Unit,
                   errorReaction: (Exception) -> Unit) {
        val url = "http://nfsfinder.cba.pl/api/user/get_within_radius.php"
        val getRacersTask = GetRacersAsyncTask(id, radius, url, validReaction, errorReaction)

        Log.d(LOGTAG, "Starting get racers task")

        getRacersTask.execute()
    }

    fun updateLoc (id: Int, lat: Double, lng: Double, errorReaction: (Exception) -> Unit) {
        val url = "http://nfsfinder.cba.pl/api/user/update_loc.php"
        val updateLocTask = UpdateLocAsyncTask(id, lat, lng, url, errorReaction)

        Log.d(LOGTAG, "Starting update loc task")

        updateLocTask.execute()
    }
}