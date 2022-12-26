package com.brosvaby.android.weatherapp.model

data class DayWeather(
    val dt_txt: String,
    val main: Main,
    val weather: List<Weather>
)

data class Main(
    val temp_min: Float,
    val temp_max: Float
)

data class Weather(
    val description: String,
    val main: String,
    val icon: String
)