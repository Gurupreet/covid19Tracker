package com.guru.covid19tracker.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.guru.covid19tracker.R
import com.guru.covid19tracker.data.firebase.FirebaseObserverType
import com.guru.covid19tracker.data.firebase.FirestoreResponseCompletionHandler
import com.guru.covid19tracker.data.firebase.syncs.NewsFirebaseSync
import com.guru.covid19tracker.models.News
import kotlinx.android.synthetic.main.news_fragment.*
import java.util.*

class NewsFragment : Fragment(), NewsListAdapter.OnListItemTapped {
    lateinit var newsListAdapter: NewsListAdapter
    var list = arrayListOf<News>()
    private var newsFirebaseSync: NewsFirebaseSync? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.news_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        startNewsFirebaseSync()
    }

    private fun setupRecyclerView() {
        newsListAdapter = NewsListAdapter(this)
        recycler_view.layoutManager  = LinearLayoutManager(recycler_view.context)
        recycler_view.adapter = newsListAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newsFirebaseSync?.stopFirebaseSync()
        newsFirebaseSync = null
    }

    private fun startNewsFirebaseSync() {
        if (newsFirebaseSync == null) {
            newsFirebaseSync = NewsFirebaseSync()
            list.clear()
            newsFirebaseSync?.startFutureFirebaseSync(object : FirestoreResponseCompletionHandler{
                override fun onSuccess(result: Any?, observerType: FirebaseObserverType) {
                    var news = result as News
                    when (observerType) {
                        FirebaseObserverType.CHILD_ADDED -> {
                            list.add(news)
                            list.reverse()
                            newsListAdapter.setData(list)
                          //  newsListAdapter.notifyDataSetChanged()
                         //   reloadData()
                        }

                        FirebaseObserverType.CHILD_CHANGED -> {
                            val iterator :MutableListIterator<News> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().id == news.id) {
                                    iterator.set(news)
                                    newsListAdapter.notifyItemChanged(index)
                                    break
                                }
                                index++
                            }
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<News> = list.listIterator()
                            var index = 0;
                            while (iterator.hasNext()) {
                                if (iterator.next().id == news.id) {
                                    iterator.remove()
                                    newsListAdapter.notifyItemRemoved(index)
                                    break
                                }
                                index++
                            }
                        }
                    }

                }

                override fun onFailure(result: Any?) {
                   // reloadData()
                }
            })

        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }

    override fun onItemTapped(news: News) {
        ImageDailogFragment.newInstance(news.attachment_url).show(childFragmentManager, "Image")
    }


}
