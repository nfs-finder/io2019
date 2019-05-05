package io2019.nfsfinder

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.activity_main.*
import io2019.nfsfinder.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    val LOG_TAG = "MainActivity"
    val ERROR_DIALOG_REQUEST = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            Log.i(LOG_TAG, "Login button clicked")
            Toast.makeText(this, "Podaj dane logowania", Toast.LENGTH_SHORT).show()

            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
        }

        if (isServiceOK()) {
            init()
        }
    }

    private fun init() : Unit {
        btnMapiczka.setOnClickListener {
            Log.i(LOG_TAG, "Idę do mapiczki")

            val intentMapiczka = Intent(this, MapsActivity::class.java)
            startActivity(intentMapiczka)
        }
    }

    private fun isServiceOK() : Boolean {
        Log.d(LOG_TAG, "isServiceOK: checking google service version")

        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        when {
            available == ConnectionResult.SUCCESS -> {
                Log.d(LOG_TAG, "isServiceOK: Google Play Services working")
                return true
            }
            GoogleApiAvailability.getInstance().isUserResolvableError(available) -> {
                Log.d(LOG_TAG, "isServiceOK: an resolvable error occured")
                val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST)
                dialog.show()
            }
            else -> Toast.makeText(this, "You can't take map requests", Toast.LENGTH_SHORT).show()
        }

        return false
    }
}
