package io2019.nfsfinder

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import io2019.nfsfinder.data.*
import kotlinx.android.synthetic.main.activity_racers.*
import io2019.nfsfinder.data.database.RequestHandler
import io2019.nfsfinder.data.model.LoggedInUser
import kotlin.concurrent.fixedRateTimer

class RacersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_racers)

        initTable()

        btnMaps.setOnClickListener {
            val intentMaps = Intent(this, MapsActivity::class.java)
            startActivity(intentMaps)
        }
    }

    fun initTable() {
        val table: TableLayout = findViewById(R.id.tableMain)
        val header = TableRow(this)
        val headerRacer = TextView(this)
        headerRacer.text = getString(R.string.table_header_racer)
        headerRacer.setTextColor(Color.BLACK)
        header.addView(headerRacer)
        val headerVehicle = TextView(this)
        headerVehicle.text = getString(R.string.table_header_vehicle)
        headerVehicle.setTextColor(Color.BLACK)
        header.addView(headerVehicle)
        val headerLatitude = TextView(this)
        headerLatitude.text = getString(R.string.table_header_latitude)
        headerLatitude.setTextColor(Color.BLACK)
        header.addView(headerLatitude)
        val headerLongitude = TextView(this)
        headerLongitude.text = getString(R.string.table_header_longitude)
        headerLongitude.setTextColor(Color.BLACK)
        header.addView(headerLongitude)

        table.addView(header)

        val racerRepo = RacerRepositorySingleton.getInstance().racerRepository

        for ((_, userInfo) in racerRepo.racerMap) {
            val user = TableRow(this@RacersActivity)
            val userName = TextView(this@RacersActivity)
            userName.text = userInfo.username
            userName.setTextColor(Color.BLACK)
            userName.gravity = Gravity.CENTER
            user.addView(userName)
            val userVehicle = TextView(this@RacersActivity)
            userVehicle.text = userInfo.car
            userVehicle.setTextColor(Color.BLACK)
            userVehicle.gravity = Gravity.CENTER
            user.addView(userVehicle)
            val userLat = TextView(this@RacersActivity)
            userLat.text = userInfo.location.latitude.toString()
            userLat.setTextColor(Color.BLACK)
            userLat.gravity = Gravity.CENTER
            user.addView(userLat)
            val userLng = TextView(this@RacersActivity)
            userLng.text = userInfo.location.longitude.toString()
            userLng.setTextColor(Color.BLACK)
            userLng.gravity = Gravity.CENTER
            user.addView(userLng)

            table.addView(user)
        }
    }
}
