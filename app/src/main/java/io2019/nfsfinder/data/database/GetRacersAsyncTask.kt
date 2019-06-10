package io2019.nfsfinder.data.database

import android.os.AsyncTask
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import io2019.nfsfinder.NFSFinderApp
import io2019.nfsfinder.data.maps.Racer
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import com.android.volley.ParseError
import org.json.JSONException
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.NetworkResponse
import com.google.android.gms.maps.model.LatLng
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset


class GetRacersAsyncTask (val id: Int, val radius: Long, val url: String,
                          val validReaction: (Set<Racer>) -> Unit, val errorReaction: (Exception) -> Unit) : AsyncTask<Unit, Unit, Unit>() {

    val LOGTAG = "GetRacersAsyncTask"

    inner class CustomJsonArrayRequest (method: Int, url: String, jsonRequest: JSONObject?,
        listener: Response.Listener<JSONArray>, errorListener: Response.ErrorListener) :
        JsonRequest<JSONArray>(method, url, jsonRequest?.toString(), listener, errorListener) {

        override fun parseNetworkResponse(response: NetworkResponse): Response<JSONArray> {
            try {
                val jsonString = String(response.data,
                    Charset.forName(HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET)))

                return Response.success(JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response))
            } catch (e: UnsupportedEncodingException) {
                return Response.error(ParseError(e))
            } catch (je: JSONException) {
                return Response.error(ParseError(je))
            }
        }
    }

    fun parseResponseToRacers (response: JSONArray) : HashSet<Racer> {
        val racersMap: HashSet<Racer> = HashSet()

        for (i in 0 until response.length()) {
            val racerJSON = response.getJSONObject(i)
            val id = racerJSON.getInt("id")
            val username = racerJSON.getString("username")
            val lng = racerJSON.getDouble("lng")
            val lat = racerJSON.getDouble("lat")
            val car = racerJSON.getString("car")

            val racer = Racer(id, username, car, LatLng(lat, lng))

            racersMap.add(racer)
        }

        return racersMap
    }

    override fun doInBackground(vararg params: Unit) {
        val jsonRequest = JSONObject()
        jsonRequest.put("id", id)
        jsonRequest.put("radius", radius)

        val jsonObjectRequest = CustomJsonArrayRequest(
            Request.Method.POST, url, jsonRequest,
            Response.Listener { response ->
                try {
                    val racers = parseResponseToRacers(response)
                    validReaction.invoke(racers)
                } catch (ex: Exception) {
                    errorReaction.invoke(ex)
                }
            },
            Response.ErrorListener { error ->
                Log.d(LOGTAG, "Error in response")
                val ex = IOException("Something went wrong")
                errorReaction.invoke(ex)
            }
        )

        val queue = RequestQueueSingleton.getInstance(NFSFinderApp.instance.applicationContext)

        Log.d(LOGTAG, "Sending request to REST server")

        queue.addToRequestQueue(jsonObjectRequest)
    }
}