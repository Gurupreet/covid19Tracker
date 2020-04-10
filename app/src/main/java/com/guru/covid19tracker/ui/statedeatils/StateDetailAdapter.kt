package com.guru.covid19tracker.ui.statedeatils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.District
import kotlinx.android.synthetic.main.district_list_item.view.*

class StateDetailAdapter: RecyclerView.Adapter<StateDetailAdapter.DistrictVH>() {
    private var dataList: List<District> = mutableListOf()
    init {
        //  setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DistrictVH {
        return DistrictVH(
            LayoutInflater.from(parent.context).inflate(R.layout.district_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(apiDataList: List<District>) {
        dataList = apiDataList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: DistrictVH, position: Int) {
        val district = dataList[position]
        holder.bind(district)
    }


    class DistrictVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(district: District) {
            itemView.apply {
                name.text = district.name
                confirmed.text = "Confirmed: ${district.confirmed}"
            }
        }

    }
}