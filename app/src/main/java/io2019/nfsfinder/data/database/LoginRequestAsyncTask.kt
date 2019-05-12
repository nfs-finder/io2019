package io2019.nfsfinder.data.database

import org.json.JSONObject
import android.os.AsyncTask
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import io2019.nfsfinder.NFSFinderApp
import io2019.nfsfinder.data.model.LoggedInUser
import java.io.IOException

class LoginRequestAsyncTask(val email: String, val password: String, val url: String,
                            val validReaction: (LoggedInUser) -> Unit,
                            val errorReaction: (Exception) -> Unit) : AsyncTask<Unit, Unit, Unit>() {

    val LOGTAG = "LoginRequestAsyncTask"

    override fun doInBackground(vararg params: Unit) {
        val jsonRequest = JSONObject()
        jsonRequest.put("email", email)
        jsonRequest.put("password", password)

        val jsonObjectRequest: JsonRequest<JSONObject> = JsonObjectRequest(Request.Method.POST, url, jsonRequest,
            Response.Listener { response ->
                try {
                    val id = response.getInt("id")
                    val username = response.getString("username")
                    val user = LoggedInUser(id, username)

                    Log.d(LOGTAG, "Successful authorisation")

                    validReaction.invoke(user)
                } catch (ex: Exception) {
                    errorReaction.invoke(ex)
                }
            },
            Response.ErrorListener {error ->
                Log.d(LOGTAG, "Error in authorisation")
                val ex: Exception
                val responseNum = error?.networkResponse?.statusCode ?: -1

                if (responseNum == 401) {
                    ex = WrongCredentialsException()
                } else {
                    ex = IOException("Something went wrong!")
                }

                errorReaction.invoke(ex)
            }
        )

        val queue = RequestQueueSingleton.getInstance(NFSFinderApp.instance.applicationContext)

        Log.d(LOGTAG, "Sending request to REST server")

        queue.addToRequestQueue(jsonObjectRequest)
    }
}