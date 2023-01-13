package com.brosvaby.android.weatherapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.brosvaby.android.weatherapp.adapter.CityAdapter
import com.brosvaby.android.weatherapp.api.ApiClient
import com.brosvaby.android.weatherapp.databinding.ActivityMainBinding
import com.brosvaby.android.weatherapp.viewmodel.CityViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cityAdapter: CityAdapter
    private lateinit var viewModel: CityViewModel
    var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CityViewModel::class.java]
        viewModel.client = ApiClient(this)
        loadingDialog = LoadingDialog(this)
        cityAdapter = CityAdapter(this@MainActivity)

        binding.rvCity.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = cityAdapter
        }

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
            when (pState) {
                is CityViewModel.CallWeather -> {
                    if (pState.city == "Seoul") viewModel.getCityList("London")
                    if (pState.city == "London") viewModel.getCityList("Chicago")
                }
            }
        })
        viewModel.getCityList2()
        viewModel.getCityList2()
    }
}