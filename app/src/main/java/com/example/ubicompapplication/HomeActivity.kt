package com.example.ubicompapplication

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var tvEngine: TextView
    private lateinit var tvEngineValue: TextView
    private lateinit var swEngine: SwitchCompat
    private lateinit var tvDoors: TextView
    private lateinit var tvDoorsValue: TextView
    private lateinit var swDoors: SwitchCompat
    private lateinit var tvClimate: TextView
    private lateinit var tvClimateValue: TextView
    private lateinit var swClimate: SwitchCompat
    private lateinit var tvTrunk: TextView
    private lateinit var tvTrunkValue: TextView
    private lateinit var swLights: SwitchCompat
    private lateinit var clEngine: ConstraintLayout
    private lateinit var clClimate: ConstraintLayout
    private lateinit var clTrunk: ConstraintLayout
    private lateinit var clDoors: ConstraintLayout
    private lateinit var bottomMenu: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvEngine = findViewById(R.id.tv_control_engine)
        tvEngineValue = findViewById(R.id.tv_control_engine_value)
        tvClimate = findViewById(R.id.tv_control_climate)
        tvClimateValue = findViewById(R.id.tv_control_climate_value)
        tvTrunk = findViewById(R.id.tv_control_lights)
        tvTrunkValue = findViewById(R.id.tv_control_trunk_value)
        tvDoors = findViewById(R.id.tv_control_doors)
        tvDoorsValue = findViewById(R.id.tv_control_doors_value)
        swEngine = findViewById(R.id.sw_engine)
        swClimate = findViewById(R.id.sw_climate)
        swLights = findViewById(R.id.sw_lights)
        swDoors = findViewById(R.id.sw_doors)
        clEngine = findViewById(R.id.cl_engine)
        clClimate = findViewById(R.id.cl_climate)
        clTrunk = findViewById(R.id.cl_trunk)
        clDoors = findViewById(R.id.cl_doors)
        bottomMenu = findViewById(R.id.nav_bottom)

        bottomMenu.menu.findItem(R.id.homeFragment).isChecked = true

        bottomMenu.menu.findItem(R.id.controlsFragment).setOnMenuItemClickListener {
            val intent = Intent(this@HomeActivity , ControlsActivity::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener false
        }

        bottomMenu.menu.findItem(R.id.stabilityFragment).setOnMenuItemClickListener {
            val intent = Intent(this@HomeActivity, StabilityActivity::class.java)
            startActivity(intent)
            return@setOnMenuItemClickListener false
        }

        if (!isPermissionsGranted(this@HomeActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val permissions = mutableSetOf(
                    Manifest.permission.NEARBY_WIFI_DEVICES
                )
                ActivityCompat.requestPermissions(this@HomeActivity, permissions.toTypedArray(), 600)
            }
        }

        requestNearbyWifiDevicesPermission()

        val preferences = this.getSharedPreferences(Constants.PREFERENCE_SMART_CAR, Context.MODE_PRIVATE)
        val edit = preferences.edit()

        if(preferences.getBoolean("engine", false)) {
            swEngine.isChecked = true
            clEngine.setBackgroundResource(R.drawable.controls_shape_on)
            tvEngine.setTextColor(Color.parseColor("#FFFFFF"))
            tvEngineValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvEngineValue.text = swEngine.textOn
        } else {
            swEngine.isChecked = false
            clClimate.setBackgroundResource(R.drawable.controls_shape)
            tvClimate.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.text = swEngine.textOff
        }

        if(preferences.getBoolean("climate", false)) {
            swClimate.isChecked = true
            clClimate.setBackgroundResource(R.drawable.controls_shape_on)
            tvClimate.setTextColor(Color.parseColor("#FFFFFF"))
            tvClimateValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvClimateValue.text = swClimate.textOn
        } else {
            swClimate.isChecked = false
            clClimate.setBackgroundResource(R.drawable.controls_shape)
            tvClimate.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.text = swClimate.textOff
        }

        if(preferences.getBoolean("doors", false)) {
            swDoors.isChecked = true
            clDoors.setBackgroundResource(R.drawable.controls_shape_on)
            tvDoors.setTextColor(Color.parseColor("#FFFFFF"))
            tvDoorsValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvDoorsValue.text = swDoors.textOn
        } else {
            swDoors.isChecked = false
            clDoors.setBackgroundResource(R.drawable.controls_shape)
            tvDoors.setTextColor(Color.parseColor("#342675"))
            tvDoorsValue.setTextColor(Color.parseColor("#342675"))
            tvDoorsValue.text = swDoors.textOff
        }

        if(preferences.getBoolean("lights", false)) {
            swLights.isChecked = true
            clTrunk.setBackgroundResource(R.drawable.controls_shape_on)
            tvTrunk.setTextColor(Color.parseColor("#FFFFFF"))
            tvTrunkValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvTrunkValue.text = swLights.textOn
        } else {
            swLights.isChecked = false
            clTrunk.setBackgroundResource(R.drawable.controls_shape)
            tvTrunk.setTextColor(Color.parseColor("#342675"))
            tvTrunkValue.setTextColor(Color.parseColor("#342675"))
            tvTrunkValue.text = swLights.textOff
        }

        swEngine.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clEngine.setBackgroundResource(R.drawable.controls_shape_on)
                tvEngine.setTextColor(Color.parseColor("#FFFFFF"))
                tvEngineValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvEngineValue.text = swEngine.textOn
            } else {
                clEngine.setBackgroundResource(R.drawable.controls_shape)
                tvEngine.setTextColor(Color.parseColor("#342675"))
                tvEngineValue.setTextColor(Color.parseColor("#342675"))
                tvEngineValue.text = swEngine.textOff
            }
            edit.putBoolean("engine", isChecked)
            edit.commit()
        }

        swClimate.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clClimate.setBackgroundResource(R.drawable.controls_shape_on)
                tvClimate.setTextColor(Color.parseColor("#FFFFFF"))
                tvClimateValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvClimateValue.text = swClimate.textOn
            } else {
                clClimate.setBackgroundResource(R.drawable.controls_shape)
                tvClimate.setTextColor(Color.parseColor("#342675"))
                tvClimateValue.setTextColor(Color.parseColor("#342675"))
                tvClimateValue.text = swClimate.textOff
            }
            edit.putBoolean("climate", isChecked)
            edit.commit()
        }

        swLights.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clTrunk.setBackgroundResource(R.drawable.controls_shape_on)
                tvTrunk.setTextColor(Color.parseColor("#FFFFFF"))
                tvTrunkValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvTrunkValue.text = swLights.textOn
            } else {
                clTrunk.setBackgroundResource(R.drawable.controls_shape)
                tvTrunk.setTextColor(Color.parseColor("#342675"))
                tvTrunkValue.setTextColor(Color.parseColor("#342675"))
                tvTrunkValue.text = swLights.textOff
            }
            edit.putBoolean("lights", isChecked)
            edit.commit()
        }

        swDoors.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clDoors.setBackgroundResource(R.drawable.controls_shape_on)
                tvDoors.setTextColor(Color.parseColor("#FFFFFF"))
                tvDoorsValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvDoorsValue.text = swDoors.textOn
            } else {
                clDoors.setBackgroundResource(R.drawable.controls_shape)
                tvDoors.setTextColor(Color.parseColor("#342675"))
                tvDoorsValue.setTextColor(Color.parseColor("#342675"))
                tvDoorsValue.text = swDoors.textOff
            }
            edit.putBoolean("doors", isChecked)
            edit.commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val preferences = this.getSharedPreferences(Constants.PREFERENCE_SMART_CAR, Context.MODE_PRIVATE)
        val edit = preferences.edit()

        if(preferences.getBoolean("engine", false)) {
            swEngine.isChecked = true
            clEngine.setBackgroundResource(R.drawable.controls_shape_on)
            tvEngine.setTextColor(Color.parseColor("#FFFFFF"))
            tvEngineValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvEngineValue.text = swEngine.textOn
        } else {
            swEngine.isChecked = false
            clClimate.setBackgroundResource(R.drawable.controls_shape)
            tvClimate.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.text = swEngine.textOff
        }

        if(preferences.getBoolean("climate", false)) {
            swClimate.isChecked = true
            clClimate.setBackgroundResource(R.drawable.controls_shape_on)
            tvClimate.setTextColor(Color.parseColor("#FFFFFF"))
            tvClimateValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvClimateValue.text = swClimate.textOn
        } else {
            swClimate.isChecked = false
            clClimate.setBackgroundResource(R.drawable.controls_shape)
            tvClimate.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.setTextColor(Color.parseColor("#342675"))
            tvClimateValue.text = swClimate.textOff
        }

        if(preferences.getBoolean("doors", false)) {
            swDoors.isChecked = true
            clDoors.setBackgroundResource(R.drawable.controls_shape_on)
            tvDoors.setTextColor(Color.parseColor("#FFFFFF"))
            tvDoorsValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvDoorsValue.text = swDoors.textOn
        } else {
            swDoors.isChecked = false
            clDoors.setBackgroundResource(R.drawable.controls_shape)
            tvDoors.setTextColor(Color.parseColor("#342675"))
            tvDoorsValue.setTextColor(Color.parseColor("#342675"))
            tvDoorsValue.text = swDoors.textOff
        }

        if(preferences.getBoolean("lights", false)) {
            swLights.isChecked = true
            clTrunk.setBackgroundResource(R.drawable.controls_shape_on)
            tvTrunk.setTextColor(Color.parseColor("#FFFFFF"))
            tvTrunkValue.setTextColor(Color.parseColor("#FFFFFF"))
            tvTrunkValue.text = swLights.textOn
        } else {
            swLights.isChecked = false
            clTrunk.setBackgroundResource(R.drawable.controls_shape)
            tvTrunk.setTextColor(Color.parseColor("#342675"))
            tvTrunkValue.setTextColor(Color.parseColor("#342675"))
            tvTrunkValue.text = swLights.textOff
        }

        swEngine.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clEngine.setBackgroundResource(R.drawable.controls_shape_on)
                tvEngine.setTextColor(Color.parseColor("#FFFFFF"))
                tvEngineValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvEngineValue.text = swEngine.textOn
            } else {
                clEngine.setBackgroundResource(R.drawable.controls_shape)
                tvEngine.setTextColor(Color.parseColor("#342675"))
                tvEngineValue.setTextColor(Color.parseColor("#342675"))
                tvEngineValue.text = swEngine.textOff
            }
            edit.putBoolean("engine", isChecked)
            edit.commit()
        }

        swClimate.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clClimate.setBackgroundResource(R.drawable.controls_shape_on)
                tvClimate.setTextColor(Color.parseColor("#FFFFFF"))
                tvClimateValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvClimateValue.text = swClimate.textOn
            } else {
                clClimate.setBackgroundResource(R.drawable.controls_shape)
                tvClimate.setTextColor(Color.parseColor("#342675"))
                tvClimateValue.setTextColor(Color.parseColor("#342675"))
                tvClimateValue.text = swClimate.textOff
            }
            edit.putBoolean("climate", isChecked)
            edit.commit()
        }

        swLights.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clTrunk.setBackgroundResource(R.drawable.controls_shape_on)
                tvTrunk.setTextColor(Color.parseColor("#FFFFFF"))
                tvTrunkValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvTrunkValue.text = swLights.textOn
            } else {
                clTrunk.setBackgroundResource(R.drawable.controls_shape)
                tvTrunk.setTextColor(Color.parseColor("#342675"))
                tvTrunkValue.setTextColor(Color.parseColor("#342675"))
                tvTrunkValue.text = swLights.textOff
            }
            edit.putBoolean("lights", isChecked)
            edit.commit()
        }

        swDoors.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                clDoors.setBackgroundResource(R.drawable.controls_shape_on)
                tvDoors.setTextColor(Color.parseColor("#FFFFFF"))
                tvDoorsValue.setTextColor(Color.parseColor("#FFFFFF"))
                tvDoorsValue.text = swDoors.textOn
            } else {
                clDoors.setBackgroundResource(R.drawable.controls_shape)
                tvDoors.setTextColor(Color.parseColor("#342675"))
                tvDoorsValue.setTextColor(Color.parseColor("#342675"))
                tvDoorsValue.text = swDoors.textOff
            }
            edit.putBoolean("doors", isChecked)
            edit.commit()
        }
    }

    private fun isPermissionsGranted(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val isNearbyDevicesPermissionGranted
        get() = hasPermission(Manifest.permission.NEARBY_WIFI_DEVICES)

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNearbyWifiDevicesPermission() {
        if(isNearbyDevicesPermissionGranted)
            return
        ActivityCompat.requestPermissions(this@HomeActivity, arrayOf(Manifest.permission.NEARBY_WIFI_DEVICES),
            Constants.LOCAL_NETWORK_CODE
        )
    }

    private fun hasPermission(permissionType: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permissionType) ==
                PackageManager.PERMISSION_GRANTED
    }

    override fun onBackPressed() {
//        super.onBackPressed()
    }
}