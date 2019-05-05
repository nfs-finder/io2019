package io2019.nfsfinder.data.database

import io2019.nfsfinder.data.model.LoggedInUser

class RequestHandler (private val dbHandler: DatabaseHandler) {
    fun requestLogin (email: String, password: String) : LoggedInUser {
        val query = "SELECT id, email, username, password FROM users WHERE email='$email';"
        val res = dbHandler.executeQuery(query)

        if (!res.next())
            throw NoSuchEmailException()

        if (res.getString("password") == password) {
            val userId = res.getInt("id")
            val username = res.getString("username")
            return LoggedInUser(userId, username)
        } else {
            throw WrongPasswordException()
        }
    }

    fun requestLocation (userId: Int) : Pair<Double, Double> {
        val uidStr = userId.toString()
        val query = "SELECT ST_X(location) AS lng, ST_Y(location) AS lat FROM users WHERE id=$uidStr;"
        val res = dbHandler.executeQuery(query)

        if (!res.next())
            throw WrongUserIdException()

        val lng = res.getDouble("lng")
        val lat = res.getDouble("lat")

        return Pair(lng, lat)
    }

    fun updateLocation (userId: Int, lng: Double, lat: Double) : Int {
        val query = "UPDATE users SET location = ST_PointFromText('POINT($lat $lng)', 4326) WHERE id=$userId;"

        return dbHandler.executeUpdate(query)
    }
}