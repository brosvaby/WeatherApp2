package com.brosvaby.android.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brosvaby.android.weatherapp.api.ApiClient
import com.brosvaby.android.weatherapp.model.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CityViewModel() : ViewModel() {
    open class ViewModelState

    var disposable: Disposable? = null
    var client: ApiClient? = null
    val items: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val states = MutableLiveData<ViewModelState>()
    val appId = "24209f3868c77f30723552dd4941e1ad"

    data class CallWeather(val city: String) : ViewModelState()

    fun getCityList(city: String) {
        isLoading.value = true
        disposable = client?.run {
            apiService.getCityForecast(city, "metric", appId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        var pre = 0
                        items.value = arrayListOf<Any>().apply {
                            add(City(it.city.name))
                            it.list.forEach { dayWeather ->
                                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                val current =
                                    LocalDate.parse(dayWeather.dt_txt, formatter).dayOfYear
                                if (pre != current) {
                                    add(dayWeather)
                                    pre = current
                                }
                            }
                        }
                        states.value = CallWeather(city)
                        isLoading.value = false
                    }, {
                        items.value = null
                        isLoading.value = false
                    }
                )
        }
    }
}