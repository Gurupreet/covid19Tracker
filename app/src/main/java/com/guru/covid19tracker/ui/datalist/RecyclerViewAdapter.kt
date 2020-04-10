package com.guru.covid19tracker.ui.datalist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.State
import com.robinhood.spark.SparkAdapter
import kotlinx.android.synthetic.main.main_list_item.view.*
import java.util.*

class RecyclerViewAdapter(val onitemTapped: OnListItemTapped): RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder>() {
    private var dataList: List<State> = arrayListOf()
    var sparkAdapterMap: HashMap<String, SparkAdapter> =
        HashMap<String, SparkAdapter>()
    private var expandedPosition = -1;
    init {
        //  setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(apiDataList: List<State>) {
        dataList = apiDataList
        notifyDataSetChanged()
    }

    fun setItemExpanded(position: Int) {
        expandedPosition = position
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val apiData = dataList[position]
        holder.bind(apiData, onitemTapped, sparkAdapterMap)
    }


    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(state: State, onitemTapped: OnListItemTapped, sparkAdapterMap:  HashMap<String, SparkAdapter>) {
            itemView?.apply {
                confirmed.text = state.confirmed.toString()
                active.text = state.active.toString()
                recovered.text = state.recovered.toString()
                deceased.text = state.deaths.toString()
                state_name.text = state.name
                increment.text = "New deaths: +${state.daily_deceased}"
                increment_cases.text = "New cases: +${state.daily_confirmed}"
                if (state.districtData.isNotEmpty()) {
                    city.text =  state.districtData[0].name+": "+state.districtData[0].confirmed

                    if (state.districtData.size > 1)
                        city1.text =  state.districtData[1].name+": "+state.districtData[1].confirmed

                    if (state.districtData.size > 2)
                        city2.text =  state.districtData[2].name+": "+state.districtData[2].confirmed
                }
                setOnClickListener { onitemTapped.onItemTapped(state) }
            }

        }
    }

    interface  OnListItemTapped {
        fun  onItemTapped(state: State)
    }
}