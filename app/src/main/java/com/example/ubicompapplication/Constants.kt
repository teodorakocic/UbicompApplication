package com.example.ubicompapplication

open class Constants {

    companion object {
        const val PREFERENCE_SMART_CAR = "smart car preference"
        const val CHANNEL_ID = "7"
        const val notificationId = 1
        const val NOTIFICATION_CODE = 100
        const val LOCAL_NETWORK_CODE = 101
        const val USERNAME = "master:mqttuser"
        const val PASSWORD = "//pass"
        const val TOPIC = "master/client-edge/attribute/#"
        const val CLIENT_ID = "client-edge"
        const val SERVER_URI = "tcp://localhost:1883"
        const val TEMPERATURE_HIGH = 35.2
        const val TEMPERATURE_LOW = 27.5
        const val AIRWAVE = 20
        const val PRESSURE_HIGH = 103.5
        const val PRESSURE_LOW = 98.8
//        const val TEMP_STREAM = "temp"
//        const val TEMP_STREAM_VALUE = "temperature"
        const val PRESSURE_STREAM = "press"
//        const val PRESSURE_STREAM_VALUE = "pressure"
//        const val LOW_TEMP_ALARM = "alarmLowTemp"
//        const val LOW_TEMP_VALUE = "avg"
//        const val HIGH_TEMP_ALARM = "alarmHighTemp"
//        const val PRESS_LIMITS_ALARM = "alarmPress"
//        const val ACC_GYRO_STREAM = "motion"
//        const val ACC_X_VALUE = "accX"
//        const val ACC_Y_VALUE = "accY"
//        const val GYRO_Z_VALUE = "gyrZ"
//        const val COLOR_STREAM = "alarmLight"
//        const val COLOR_STREAM_VALUE = "brightness"
//        const val GESTURE_STREAM = "gesture"
//        const val LPS_STREAM = "lps"
//        const val PROXIMITY_STREAM = "alarmProximity"
//        const val PROXIMITY_STREAM_VALUE = "proximity"
//        const val DETECTION_STREAM = "personDetection"

        const val TEMP_STREAM_VALUE = "subscribeTemperature"
        const val PRESSURE_STREAM_VALUE = "subscribePressure"
        const val OVER_LIMIT_PRESSURE_VALUE = "alarmPressure"
        const val LOW_TEMP_VALUE = "alarmTemperatureLow"
        const val HIGH_TEMP_VALUE = "alarmTemperatureHigh"
        const val ACC_X_VALUE = "subscribeAccX"
        const val ACC_Y_VALUE = "subscribeAccY"
        const val GYRO_Z_VALUE = "subscribeGyroZ"
        const val COLOR_STREAM_VALUE = "alarmBrightness"
        const val PROXIMITY_STREAM_VALUE = "alarmProximity"
        const val NOTIFICATION_VALUE = "subscribeMessage"

        const val DETECTION_STREAM_VALUE = "detected"
        const val ROAD_CURVE_LIMIT = 0.15
        const val ACC_CURVE_LIMIT = 0.5
        const val LOCATION_PERMISSION_REQUEST_CODE = 2
        const val LED_SERVICE = "19B10010-E8F2-537E-4F6C-D104768A1214"
        const val LED_CHARACTERISTIC = "19B10011-E8F2-537E-4F6C-D104768A1214"
        const val BUTTON_CHARACTERISTIC = "19B10012-E8F2-537E-4F6C-D104768A1214"
    }

}