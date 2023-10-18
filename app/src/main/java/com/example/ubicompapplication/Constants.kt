package com.example.ubicompapplication

open class Constants {

    companion object {
        const val PREFERENCE_SMART_CAR = "smart car preference"
        const val CHANNEL_ID = "7"
        const val notificationId = 1
        const val NOTIFICATION_CODE = 100
        const val LOCAL_NETWORK_CODE = 101
        const val TOPIC = "edge/#"
        const val CLIENT_ID = "client-edge"
        const val SERVER_URI = "tcp://192.168.1.104:1884"
        const val TEMPERATURE_HIGH = 32.5
        const val TEMPERATURE_LOW = 24.5
        const val AIRWAVE = 20
        const val PRESSURE_HIGH = 103.5
        const val PRESSURE_LOW = 99.0
        const val PARKING_SENSORS_VALUE = 32.0

        const val TEMP_STREAM_VALUE = "subscribeTemperature"
        const val PRESSURE_STREAM_VALUE = "subscribePressure"
        const val OVER_LIMIT_PRESSURE_VALUE = "alarmPressure"
        const val LOW_TEMP_VALUE = "alarmTemperatureLow"
        const val HIGH_TEMP_VALUE = "alarmTemperatureHigh"
        const val ACC_X_VALUE = "aX"
        const val ACC_Y_VALUE = "aY"
        const val GYRO_Z_VALUE = "gZ"
        const val MOTION_VALUE = "subscribeMotion"
        const val COLOR_STREAM_VALUE = "alarmBrightness"
        const val PROXIMITY_STREAM_VALUE = "subscribeProximity"
        const val NOTIFICATION_VALUE = "alarmDetection"

        const val ROAD_CURVE_LIMIT = 0.15
        const val ACC_CURVE_LIMIT = 0.2
        const val LOCATION_PERMISSION_REQUEST_CODE = 2
        const val LED_SERVICE = "19B10010-E8F2-537E-4F6C-D104768A1214"
        const val LED_CHARACTERISTIC = "19B10011-E8F2-537E-4F6C-D104768A1214"
        const val BUTTON_CHARACTERISTIC = "19B10012-E8F2-537E-4F6C-D104768A1214"
    }

}