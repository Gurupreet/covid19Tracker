package com.guru.covid19tracker.ui.info

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.Info
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.LinkBuilder
import kotlinx.android.synthetic.main.info_list_item.view.*
import java.text.ParseException

class InfoListAdapter(val onitemTapped: OnListItemTapped): RecyclerView.Adapter<InfoListAdapter.InfoViewHolder>() {
    private var dataList: List<Info> = mutableListOf()
    init {
        //  setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): InfoViewHolder {
        return InfoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.info_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(apiDataList: List<Info>) {
        dataList = apiDataList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        val news = dataList[position]
        holder.bind(news, onitemTapped)
    }


    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mWeblink: Link = Link(Patterns.WEB_URL)

        init {
            mWeblink.textColor = ContextCompat.getColor(itemView.context, R.color.colorAccent)
            mWeblink.highlightAlpha = .4f
            mWeblink.isUnderlined = false
            mWeblink.setOnClickListener { clickedText -> openLink(clickedText, itemView.context) }
        }
        fun bind(info: Info, onitemTapped: OnListItemTapped) {
            itemView.apply {
                title.text =  info.title
                content.text = info.content

                if (!info.title.isNullOrEmpty()) {
                    LinkBuilder.on(title).addLink(mWeblink).build()
                    LinkBuilder.on(content).addLink(mWeblink).build()
                }

                if (!info.content2.isNullOrEmpty()) {
                    content2.visibility = View.VISIBLE
                    content2.text = info.content2
                    LinkBuilder.on(content2).addLink(mWeblink).build()
                } else {
                    content2.visibility = View.GONE
                }


                if (!info.content3.isNullOrEmpty()) {
                    content3.visibility = View.VISIBLE
                    content3.text = info.content3
                    LinkBuilder.on(content3).addLink(mWeblink).build()
                } else {
                    content3.visibility = View.GONE
                }
            }
        }

        private fun openLink(url: String?, context: Context) {
            try {
                val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
                val customTabsIntent: CustomTabsIntent = builder.build()
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.launchUrl(context, Uri.parse(url))
            } catch (e: ActivityNotFoundException) {

            } catch (ex: ParseException) {

            }
        }
    }

    interface  OnListItemTapped {
        fun  onItemTapped(state: Info)
    }
}