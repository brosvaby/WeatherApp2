package com.brosvaby.android.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brosvaby.android.weatherapp.api.ApiClient
import com.brosvaby.android.weatherapp.model.City
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class CityViewModel() : ViewModel() {
    open class ViewModelState

    var disposable: Disposable? = null
    var disposable2: Disposable? = null
    var client: ApiClient? = null
    val items: MutableLiveData<ArrayList<Any>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val states = MutableLiveData<ViewModelState>()
    val appId = "24209f3868c77f30723552dd4941e1ad"

    data class CallWeather(val city: String) : ViewModelState()

    // observable
    fun getCityListByStandard() {
        client?.apiService?.run {
            val ob = getCityForecastByStandard("Seoul", "metric", appId)
            ob.subscribe({
                /**
                 * 
                 */

            }, {
                // onError

            }, {
                // onCompleted

            })
        }
        val seoulWork = client?.apiService?.getCityForecast("Seoul", "metric", appId)
            ?.delay(500, TimeUnit.MILLISECONDS)

        seoulWork?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
    }

    // single
    fun getCityList2() {
        disposable?.run {
            dispose()
            Log.d("jwlee", "취소 합니다.")
        }
        val seoulWork = client?.apiService?.getCityForecast("Seoul", "metric", appId)
            ?.delay(500, TimeUnit.MILLISECONDS)
        val londonWork = client?.apiService?.getCityForecast("London", "metric", appId)
        val chicagoWork = client?.apiService?.getCityForecast("Chicago", "metric", appId)

        isLoading.value = true
        disposable = Single.concat(seoulWork, londonWork, chicagoWork)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
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
            }, {
                items.value = arrayListOf()
                isLoading.value = false
            }, {
                Log.d("jwlee", "호출이 완료 되었습니다.")
                items.value = arrayListOf()
                isLoading.value = false
            })
    }

    // single
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
                        items.value = arrayListOf()
                        isLoading.value = false
                    }
                )
        }
    }
}