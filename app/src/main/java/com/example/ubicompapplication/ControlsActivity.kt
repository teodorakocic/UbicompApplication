package com.example.ubicompapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.eclipse.paho.client.mqttv3.MqttMessage


class ControlsActivity : AppCompatActivity() {

    private lateinit var tvCurrentTemperature: TextView
    private lateinit var tvCurrentPressure: TextView
    private lateinit var tvHeatingValue: TextView
    private lateinit var tvCoolingValue: TextView
    private lateinit var tvAirwaveValue: TextView
    private lateinit var tvFanValue: TextView
    private lateinit var tvCoolerValue: TextView
    private lateinit var clHeating: ConstraintLayout
    private lateinit var clCooling: ConstraintLayout
    private lateinit var clVentilation: ConstraintLayout
    private lateinit var bottomMenu: BottomNavigationView
    private var currentTemperature: Double = 0.0
    private var currentPressure: Double = 0.0
    lateinit var mqttClient: MqttConnection
    private lateinit var preferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controls)

        tvCurrentTemperature = findViewById(R.id.tv_temperature_value)
        tvCurrentPressure = findViewById(R.id.tv_pressure_value)
        tvHeatingValue = findViewById(R.id.tv_heating_value)
        tvCoolingValue = findViewById(R.id.tv_cooling_value)
        tvAirwaveValue = findViewById(R.id.tv_airwave_value)
        tvFanValue = findViewById(R.id.tv_fan_value)
        tvCoolerValue = findViewById(R.id.tv_cooler_value)
        clHeating = findViewById(R.id.cl_heating)
        clCooling = findViewById(R.id.cl_cooling)
        clVentilation = findViewById(R.id.cl_airwave)
        bottomMenu = findViewById(R.id.nav_bottom)

        bottomMenu.menu.findItem(R.id.controlsFragment).isChecked = true

        bottomMenu.menu.findItem(R.id.homeFragment).setOnMenuItemClickListener {
            val intent = Intent(this@ControlsActivity, HomeActivity::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener false
        }

        bottomMenu.menu.findItem(R.id.stabilityFragment).setOnMenuItemClickListener {
            val intent = Intent(this@ControlsActivity, StabilityActivity::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener false
        }

        preferences = this.getSharedPreferences(Constants.PREFERENCE_SMART_CAR, Context.MODE_PRIVATE)
        edit = preferences.edit()

        tvHeatingValue.text = "${Constants.TEMPERATURE_HIGH} C"
        tvCoolingValue.text = "${Constants.TEMPERATURE_LOW} C"
        tvAirwaveValue.text = "${Constants.AIRWAVE} C"

        if(preferences.getBoolean("engine", false))
        {
            mqttClient = MqttConnection(applicationContext, arrayOf(Constants.TOPIC), IntArray(1) { 0 })
            mqttClient.init()
            initMqttStatusListener()
            mqttClient.connect()
        } else {
            engineStoppedUI()
            Toast.makeText(this@ControlsActivity, "Start Your car for monitoring temperature and pressure values!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initMqttStatusListener() {
        mqttClient.mqttStatusListener = object : MqttStatusListener {

            override fun onConnectFailure(exception: Throwable) {
                displayInDebugLog("Failed to connect")
            }

            override fun onConnectionLost(exception: Throwable) {
                displayInDebugLog("The Connection was lost.")
            }

            @SuppressLint("SetTextI18n")
            override fun onMessageArrived(topic: String, message: MqttMessage) {
                if(topic.contains(Constants.TEMP_STREAM_VALUE)) {
                    receivedTemperature(message)
                }
                if(topic.contains(Constants.PRESSURE_STREAM_VALUE)) {
                    receivedPressure(message)
                }
                if(topic.contains(Constants.HIGH_TEMP_VALUE)) {
                    receivedAlarmForHighTemperature(message)
                }
                if(topic.contains(Constants.LOW_TEMP_VALUE)) {
                    receivedAlarmForLowTemperature(message)
                }
                if(topic.contains(Constants.OVER_LIMIT_PRESSURE_VALUE)) {
                    receivedAlarmForPressureLimits(message)
                }
                displayInMessagesList(String(message.payload))
            }

            override fun onDisconnectService() {
                displayInDebugLog("The Connection was stopped.")
            }

            override fun onTopicSubscriptionSuccess() {
                displayInDebugLog("Subscribed!")
            }

            override fun onTopicSubscriptionError(exception: Throwable) {
                displayInDebugLog("Failed to subscribe")
            }
        }
    }

    private fun displayInMessagesList(message: String) {
        Log.d("new received message", message)
    }

    private fun displayInDebugLog(message: String) {
        Log.i("display in debug", message)
    }

    @SuppressLint("SetTextI18n")
    private fun receivedTemperature(message: MqttMessage) {
        tvCurrentTemperature.text = "${String(message.payload)} C"
        currentTemperature = String(message.payload).toDouble()
        if(currentTemperature < Constants.TEMPERATURE_LOW) {
            clHeating.setBackgroundResource(R.drawable.controls_shape_heating_on)
            tvCoolerValue.text = "On"
        }
        if(currentTemperature > Constants.TEMPERATURE_HIGH) {
            clCooling.setBackgroundResource(R.drawable.controls_shape_cooling_on)
            tvCoolerValue.text = "On"
        }
        if(currentTemperature in Constants.TEMPERATURE_LOW..Constants.TEMPERATURE_HIGH) {
            if(!preferences.getBoolean("climate", false)) {
                clHeating.setBackgroundResource(R.drawable.controls_shape)
                clCooling.setBackgroundResource(R.drawable.controls_shape)
                tvCoolerValue.text = "Off"
                tvCoolerValue.setTextColor(Color.parseColor("#342675"))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun receivedPressure(message: MqttMessage) {
        tvCurrentPressure.text = "${String(message.payload)} kPa"
        currentPressure= String(message.payload).toDouble()
        if(currentPressure in Constants.PRESSURE_LOW .. Constants.PRESSURE_HIGH) {
            if(preferences.getBoolean("climate", false)) {
                clVentilation.setBackgroundResource(R.drawable.controls_shape)
                tvFanValue.text = "Off"
                tvFanValue.setTextColor(Color.parseColor("#342675"))
            }
        } else {
            clVentilation.setBackgroundResource(R.drawable.controls_shape_ventilation_on)
            tvFanValue.text = "On"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun receivedAlarmForHighTemperature(message: MqttMessage) {
        if(!preferences.getBoolean("doors", false)) {
            Toast.makeText(this@ControlsActivity, "Close the door to make the air conditioner work more efficiently!", Toast.LENGTH_SHORT).show()
        }
//        if(readRuleValue(String(message.payload), Constants.TEMP_STREAM_VALUE).toDouble() > Constants.TEMPERATURE_HIGH) {
        if(String(message.payload).toBoolean()) {
            clHeating.setBackgroundResource(R.drawable.controls_shape)
            clCooling.setBackgroundResource(R.drawable.controls_shape_cooling_on)
            tvCoolerValue.text = "Cold"
            tvCoolerValue.setTextColor(Color.parseColor("#00CCFF"))
            edit.putBoolean("climate", true)
            edit.commit()
            Handler(Looper.getMainLooper()).postDelayed({
                edit.putBoolean("climate", false)
                edit.commit()
            }, 3_000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun receivedAlarmForLowTemperature(message: MqttMessage) {
        if(!preferences.getBoolean("doors", false)) {
            Toast.makeText(this@ControlsActivity, "Close the door to make the air conditioner work more efficiently!", Toast.LENGTH_SHORT).show()
        }
//        if(readRuleValue(String(message.payload), Constants.LOW_TEMP_VALUE).toDouble() < Constants.TEMPERATURE_LOW) {
        if(String(message.payload).toBoolean()) {
            clCooling.setBackgroundResource(R.drawable.controls_shape)
            clHeating.setBackgroundResource(R.drawable.controls_shape_heating_on)
            tvCoolerValue.text = "Heat"
            tvCoolerValue.setTextColor(Color.parseColor("#E3242B"))
            edit.putBoolean("climate", true)
            edit.commit()
            Handler(Looper.getMainLooper()).postDelayed({
                edit.putBoolean("climate", false)
                edit.commit()
            }, 3_000)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun receivedAlarmForPressureLimits(message: MqttMessage) {
        if(!preferences.getBoolean("doors", false)) {
            Toast.makeText(this@ControlsActivity, "Close the door to make the fan work more efficiently!", Toast.LENGTH_SHORT).show()
        }
//        if(readRuleValue(String(message.payload), Constants.PRESSURE_STREAM_VALUE).toDouble() !in Constants.PRESSURE_LOW..Constants.PRESSURE_HIGH) {
        if(String(message.payload).toBoolean()) {
            clVentilation.setBackgroundResource(R.drawable.controls_shape_ventilation_on)
            tvFanValue.text = "On"
            tvFanValue.setTextColor(Color.parseColor("#90EE90"))
            edit.putBoolean("climate", true)
            edit.commit()
            Handler(Looper.getMainLooper()).postDelayed({
                edit.putBoolean("climate", false)
                edit.commit()
            }, 4_500)
        }
    }

    private fun engineStoppedUI() {
        tvCurrentTemperature.text = "--"
        tvCurrentPressure.text = "--"
        tvHeatingValue.text = "--"
        tvCoolingValue.text = "--"
        tvAirwaveValue.text = "--"
    }

//    private fun readRuleValue(payload: String, key: String): String {
//        val jsonObject: JsonObject = Json.decodeFromString(payload)
//        return jsonObject[key].toString()
//    }

    override fun onDestroy() {
        super.onDestroy()
        mqttClient.disconnect()
    }
}