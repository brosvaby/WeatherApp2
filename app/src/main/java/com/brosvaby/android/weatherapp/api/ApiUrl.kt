package com.brosvaby.android.weatherapp.api

private const val DOMAIN_URL = "https://api.openweathermap.org"
val BASE_URL_API = getBaseUrl()

const val Forecast = "/data/2.5/forecast"

private fun getBaseUrl(): String {
    val baseUrl = DOMAIN_URL
    return baseUrl
}