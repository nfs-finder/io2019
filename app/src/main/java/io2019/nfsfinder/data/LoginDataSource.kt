package io2019.nfsfinder.data

import io2019.nfsfinder.data.model.LoggedInUser
import io2019.nfsfinder.data.database.RequestHandler
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource (private val handler: RequestHandler) {
    fun login(email: String, password: String): Result<LoggedInUser> {
        try {
            val user = handler.requestLogin(email, password)
            return Result.Success(user)
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
    }
}

