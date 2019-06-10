package io2019.nfsfinder.data.login

import android.util.Log
import io2019.nfsfinder.data.database.RequestHandler
import io2019.nfsfinder.data.database.RequestHandlerSingleton
import kotlin.properties.Delegates

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val handler: RequestHandler = RequestHandlerSingleton.getInstance().requestHandler
    val LOGTAG = "LoginDataSource"

    var loginResult: Result<LoggedInUser>? by Delegates.observable<Result<LoggedInUser>?>(null) { _, _, new ->
        onResultChange?.invoke(new)
    }

    var onResultChange: ((Result<LoggedInUser>?) -> Unit)? = null

    fun login(email: String, password: String) {
        val validReaction: (LoggedInUser) -> Unit = {
            Log.d(LOGTAG, "Successful authorisation")
            loginResult = Result.Success(it)
        }
        val errorReaction: (Exception) -> Unit = {
            Log.d(LOGTAG, "Error in authorisation")
            loginResult = Result.Error(it)
        }

        handler.requestLogin(email, password, validReaction, errorReaction)
    }

    fun logout() {
    }
}

