package com.example.weatherapp.current_weather_feature.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
class CurrentWeatherAdapter(
    private val dataSet: List<String>
    ) : RecyclerView.Adapter<CurrentWeatherAdapter.QueryViewHolder>() {

    inner class QueryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        return QueryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_query,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: QueryViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvQuery).text = dataSet[position]
            setOnClickListener {
                onItemClickListener?.let { it(dataSet[position]) }
            }
        }
    }

    override fun getItemCount() = dataSet.size

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}