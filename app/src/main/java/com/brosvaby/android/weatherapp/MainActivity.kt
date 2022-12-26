package com.brosvaby.android.weatherapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brosvaby.android.weatherapp.adapter.CityAdapter
import com.brosvaby.android.weatherapp.api.ApiClient
import com.brosvaby.android.weatherapp.viewmodel.CityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var cityAdapter: CityAdapter
    private lateinit var viewModel: CityViewModel
    var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCity = findViewById<RecyclerView>(R.id.rv_city)
        viewModel = ViewModelProvider(this)[CityViewModel::class.java]
        viewModel.client = ApiClient(this)
        loadingDialog = LoadingDialog(this)

        rvCity.layoutManager = LinearLayoutManager(this)
        cityAdapter = CityAdapter(MainActivity@ this)
        rvCity.adapter = cityAdapter

        var resultObserver = Observer<ArrayList<Any>> { result ->
            cityAdapter.addAll(result)
        }
        val isLoadingObserver = Observer<Boolean> { isLoading ->
            loadingDialog?.run {
                if (isLoading) show() else dismiss()
            }
        }
        viewModel.items.observe(this, resultObserver)
        viewModel.isLoading.observe(this, isLoadingObserver)
        viewModel.states.observe(this, Observer { pState ->
            when(pState) {
                is CityViewModel.CallWeather -> {
                    if(pState.city == "London") viewModel.getCityList("Seoul")
                    if(pState.city == "Seoul") viewModel.getCityList("Chicago")
                }
            }
        })
        viewModel.getCityList("London")
    }
}