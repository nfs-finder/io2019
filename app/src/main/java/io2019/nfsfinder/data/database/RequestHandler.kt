package io2019.nfsfinder.data.database

import android.util.Log
import io2019.nfsfinder.data.model.LoggedInUser


class RequestHandler {
    val url = "http://nfsfinder.cba.pl/api/user/login.php"
    val LOGTAG = "RequestHandler"

    fun requestLogin (email: String, password: String, validReaction: (LoggedInUser) -> Unit,
                      errorReaction: (Exception) -> Unit) {
        val loginTask = LoginRequestAsyncTask(email, password, url, validReaction, errorReaction)

        Log.d(LOGTAG, "Starting login task")

        loginTask.execute()
    }
}