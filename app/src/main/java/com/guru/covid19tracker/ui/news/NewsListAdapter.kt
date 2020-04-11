package com.guru.covid19tracker.ui.news

import android.content.ActivityNotFoundException
import android.content.Context
import android.net.ParseException
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.News
import com.guru.covid19tracker.utils.Utility
import com.klinker.android.link_builder.Link
import com.klinker.android.link_builder.LinkBuilder
import kotlinx.android.synthetic.main.news_list_item.view.*

class NewsListAdapter(val onitemTapped: OnListItemTapped): RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {
    private var dataList: List<News> = mutableListOf()
    init {
        //  setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): NewsViewHolder {
        return NewsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.news_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(apiDataList: List<News>) {
        dataList = apiDataList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = dataList[position]
        holder.bind(news, onitemTapped)
    }


    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mWeblink: Link
        init {
            mWeblink = Link(Patterns.WEB_URL)
            mWeblink.textColor = ContextCompat.getColor(itemView.context, R.color.colorAccent)
            mWeblink.highlightAlpha = .4f
            mWeblink.isUnderlined = false
            mWeblink.setOnClickListener { clickedText -> openLink(clickedText, itemView.context) }
        }
        fun bind(news: News, onitemTapped: OnListItemTapped) {
                itemView.apply {
                    username.text =  news.sender_name
                    message_text.text = news.message_text
                    source.text = "Source: ${news.source}"
                    time_stamp.text = Utility.getTimeAgo(news.created_at, time_stamp.context)
                    Glide.with(context)
                        .load(news.avatar_url)
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.light_grey)
                        .into(avatar_image)
                    Glide.with(context)
                        .load(news.attachment_url)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.color.light_grey)
                        .into(photo_message)

                    if (news.video_url.isNullOrEmpty()) {
                        play_button.visibility = View.GONE
                        photo_message.setOnClickListener {
                            onitemTapped.onItemTapped(news)
                        }
                    } else {
                        play_button.visibility = View.VISIBLE
                        photo_message.setOnClickListener {
                            openLink(news.video_url, itemView.context)
                        }
                    }



                    if (!news.message_text.isNullOrEmpty()) {
                        LinkBuilder.on(message_text).addLink(mWeblink).build()
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
                Log.d("exception", e.localizedMessage)
            } catch (ex: ParseException) {
                Log.d("exception", ex.localizedMessage)
            }
        }
    }

    interface  OnListItemTapped {
        fun  onItemTapped(state: News)
    }
}