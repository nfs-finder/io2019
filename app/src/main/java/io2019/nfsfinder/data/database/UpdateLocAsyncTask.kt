package io2019.nfsfinder.data.database

import android.os.AsyncTask
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import io2019.nfsfinder.NFSFinderApp
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException

class UpdateLocAsyncTask (val id: Int, val lat: Double, val lng: Double, val url: String, val errorReaction: (Exception) -> Unit) :
    AsyncTask<Unit, Unit, Unit>() {

    val LOGTAG = "UpdateLocAsyncTask"

    inner class RequestForEmpty(method: Int, url:String, jsonRequest: JSONObject,
                                listener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener) :
        JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {

        override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
            var res = response

            try {
                if (res.data.isEmpty()) {
                    val responseData = "{}".toByteArray(charset("UTF8"))
                    res = NetworkResponse(response.statusCode, responseData, response.headers, response.notModified)
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            return super.parseNetworkResponse(res)
        }
    }
    override fun doInBackground(vararg params: Unit?) {
        val jsonRequest = JSONObject()
        jsonRequest.put("id", id)
        jsonRequest.put("lng", lng)
        jsonRequest.put("lat", lat)

        val req = RequestForEmpty(Request.Method.POST, url, jsonRequest,
            Response.Listener {
                Log.d(LOGTAG, "Successfully updated location")
            }, Response.ErrorListener { error ->
                Log.d(LOGTAG, "Error in response")
                val ex = IOException("Something went wrong")
                errorReaction.invoke(ex)
            })

        val queue = RequestQueueSingleton.getInstance(NFSFinderApp.instance.applicationContext)

        Log.d(LOGTAG, "Sending request to REST server")

        queue.addToRequestQueue(req)
    }
}