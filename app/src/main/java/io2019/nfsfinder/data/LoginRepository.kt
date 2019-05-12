package io2019.nfsfinder.data

import android.util.Log
import io2019.nfsfinder.data.model.LoggedInUser
import kotlin.properties.Delegates

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {
    val LOGTAG = "LoginRepository"

    var loginResult: Result<LoggedInUser>? by Delegates.observable<Result<LoggedInUser>?>(null) { _, _, new ->
        onResultChange?.invoke(new)
    }

    var onResultChange: ((Result<LoggedInUser>?) -> Unit)? = null

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String) {
        // handle login
        dataSource.onResultChange = {
            val res: Result<LoggedInUser> = it!!

            if (res is Result.Success) {
                Log.d(LOGTAG, "Setting logged in user")
                setLoggedInUser(res.data)
            } else {
                Log.d(LOGTAG, "Error in authorisation, passing info further")
            }

            loginResult = res
        }

        dataSource.login(username, password)
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}
