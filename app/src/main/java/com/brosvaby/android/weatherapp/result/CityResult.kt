package com.brosvaby.android.weatherapp.result

import com.brosvaby.android.weatherapp.model.City
import com.brosvaby.android.weatherapp.model.DayWeather

data class CityResult(
    var city: City,
    var list : List<DayWeather>
)