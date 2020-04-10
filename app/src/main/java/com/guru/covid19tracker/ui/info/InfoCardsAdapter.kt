package com.guru.covid19tracker.ui.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.Info
import com.guru.covid19tracker.utils.CardType

import kotlinx.android.synthetic.main.info_list_item.view.title
import kotlinx.android.synthetic.main.row_info_cards.view.*

class InfoCardsAdapter(val type: CardType): RecyclerView.Adapter<InfoCardsAdapter.InfoCardViewHolder>() {
    private var dataList: List<Info> = mutableListOf()

    init {
        //  setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): InfoCardViewHolder {
        return InfoCardViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_info_cards, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(apiDataList: List<Info>) {
        dataList = apiDataList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: InfoCardViewHolder, position: Int) {
        val news = dataList[position]
        holder.bind(news, type)
    }


    class InfoCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(info: Info, type: CardType) {
            itemView?.apply {
                title.text = info.title
                subtitle.text = info.content

                when (type) {
                    CardType.INFO -> wrapper.background = ContextCompat.getDrawable(itemView.context, R.drawable.gradient_background_blue)
                    CardType.SYMPTOMS -> wrapper.background = ContextCompat.getDrawable(itemView.context, R.drawable.gradient_background_orange)
                }
            }
        }
    }

}