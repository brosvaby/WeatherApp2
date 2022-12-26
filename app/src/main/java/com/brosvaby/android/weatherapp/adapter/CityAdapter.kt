package com.brosvaby.android.weatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brosvaby.android.weatherapp.R
import com.brosvaby.android.weatherapp.model.City
import com.brosvaby.android.weatherapp.model.DayWeather
import com.bumptech.glide.Glide
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.collections.ArrayList

class CityAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class CardType{ Header, City}
    private var items: ArrayList<Any> = arrayListOf()

    fun addAll(list: List<Any>) {
        val preSize = items.size
        items.addAll(list)
        notifyItemRangeChanged(preSize, items.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context: Context = parent.context

        return if(viewType == CardType.Header.ordinal) {
            val view = LayoutInflater.from(context).inflate(R.layout.header_item, parent, false)
            CityViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.view_item, parent, false)
            DayWeatherViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DayWeatherViewHolder){
            holder.bind(items[position] as DayWeather, position)
        } else if(holder is CityViewHolder) {
            holder.bind(items[position] as City)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return if(item is City) {
            CardType.Header.ordinal
        } else  {
            CardType.City.ordinal
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tv_name)

        fun bind(item: City) {
            tvName.text = item.name
        }
    }

    inner class DayWeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay = itemView.findViewById<TextView>(R.id.tv_day)
        private val ivWeather = itemView.findViewById<ImageView>(R.id.iv_weather)
        private val tvStatus = itemView.findViewById<TextView>(R.id.tv_status)
        private val tvMax = itemView.findViewById<TextView>(R.id.tv_max)
        private val tvMin = itemView.findViewById<TextView>(R.id.tv_min)

        fun bind(item: DayWeather, index: Int) {
            val idx = index % 7
            tvDay.text = if(idx == 1) {
                "Today"
            } else if(idx == 2) {
                "Tomorrow"
            } else {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                val current = LocalDate.parse(item.dt_txt, formatter).dayOfWeek
                current.getDisplayName(TextStyle.SHORT, Locale.US)
            }
            tvStatus.text = item.weather[0].main
            tvMax.text = "Max: ${item.main.temp_max.toInt()} ℃"
            tvMin.text = "Min: ${item.main.temp_min.toInt()} ℃"

            Glide.with(context)
                .load("https://openweathermap.org/img/wn/${item.weather[0].icon}.png")
                .into(ivWeather)
        }
    }
}