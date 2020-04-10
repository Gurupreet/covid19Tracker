package com.guru.covid19tracker.ui.datalist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.State
import com.guru.covid19tracker.data.firebase.FirebaseObserverType
import com.guru.covid19tracker.data.firebase.FirestoreResponseCompletionHandler
import com.guru.covid19tracker.data.firebase.syncs.StatesFirestoreSync
import com.guru.covid19tracker.models.District
import com.guru.covid19tracker.ui.statedeatils.StateDetailActivity
import com.guru.covid19tracker.utils.Filter
import com.guru.covid19tracker.utils.Utility
import kotlinx.android.synthetic.main.data_list_fragment.*

class DataListFragment : Fragment(), RecyclerViewAdapter.OnListItemTapped, SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private var statesFirestoreSync: StatesFirestoreSync? = null
    private val list = arrayListOf<State>()
    private var filter = Filter.CONFIRMED
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startStateFirebaseSync()
        return inflater.inflate(R.layout.data_list_fragment, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        statesFirestoreSync?.stopFirestoreSync()
        statesFirestoreSync = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        recyclerViewAdapter =
            RecyclerViewAdapter(this)
        recycler_view.layoutManager = LinearLayoutManager(context)
        recycler_view.adapter = recyclerViewAdapter

        swipe_container.setOnRefreshListener(this)
        swipe_container.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        sort.setOnClickListener {
            showPopupMenu()
        }
        filter_text.setOnClickListener {
            showPopupMenu()
        }
        setupFilterText()
    }

    fun setupFilterText() {
        when(filter) {
            Filter.CONFIRMED -> filter_text.text = "Confirmed"
            Filter.RECOVERED -> filter_text.text = "Recovered"
            Filter.DEATHS -> filter_text.text = "Deaths"
            Filter.INCREMENT -> filter_text.text = "New cases"
        }
    }

    fun setupTopCard(state: State) {
        confirmed.text = state.confirmed.toString()
        active.text = state.active.toString()
        recovered.text = state.recovered.toString()
        deceased.text = state.deaths.toString()
        updated.text = "Last updates: "+Utility.getTimeAgo(state.updatedAt, updated.context)
        daily_confirmed.text = "New +${state.daily_confirmed}"
        daily_deceased.text = "New +${state.daily_deceased}"
        daily_recovered.text = "New +${state.daily_recovered}"
        daily_active.text = "New +${state.daily_confirmed - state.daily_deceased- state.daily_recovered}"
        if (state.extra.isNotEmpty()) {
            extra.text = state.extra
        }
        val confirmedAdapter =
            SparkLineAdapter(state.confirmed_history, state.confirmed)
        val activeAdapter =
            SparkLineAdapter(state.active_history, state.active)

        val deathAdapter =
            SparkLineAdapter(state.death_history, state.confirmed/10)
        val recoverdAdapter =
            SparkLineAdapter(state.recovered_history, state.confirmed/10)

        confirmed_spark.adapter = confirmedAdapter
        confirmed_spark.baseLineColor = ContextCompat.getColor(context!!, R.color.colorAccent)
        confirmed_spark.lineColor = ContextCompat.getColor(context!!, R.color.colorAccent)

        active_spark.adapter = confirmedAdapter
        active_spark.lineColor = ContextCompat.getColor(context!!, R.color.orange)
        active_spark.baseLineColor = ContextCompat.getColor(context!!, R.color.orange)

        recovered_spark.adapter = recoverdAdapter
        recovered_spark.lineColor = ContextCompat.getColor(context!!, R.color.green)
        recovered_spark.baseLineColor = ContextCompat.getColor(context!!, R.color.green)

        deceased_spark.adapter = deathAdapter
        deceased_spark.lineColor = ContextCompat.getColor(context!!, R.color.red)
        deceased_spark.baseLineColor = ContextCompat.getColor(context!!, R.color.red)

    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(context!!, sort)
        popupMenu.inflate(R.menu.sort_menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when (it?.itemId) {
                R.id.recovered -> {
                    filter = Filter.RECOVERED
                    restartSync()
                    return@setOnMenuItemClickListener true
                }
                R.id.confirmed -> {
                    //close all trades
                    filter = Filter.CONFIRMED
                    restartSync()
                    return@setOnMenuItemClickListener true
                }
                R.id.death -> {
                    //learn
                    filter = Filter.DEATHS
                    restartSync()
                    return@setOnMenuItemClickListener true
                }
                R.id.new_cases -> {
                    filter = Filter.INCREMENT
                    list.sortedBy { it.daily_confirmed }
                    reloadData()
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener true
                }
            }
        }
    }

    private fun startStateFirebaseSync() {
        if (statesFirestoreSync == null) {
            list.clear()
            if (::recyclerViewAdapter.isInitialized) { recyclerViewAdapter.setData(list) }
            statesFirestoreSync =
                StatesFirestoreSync(
                    filter
                )
            statesFirestoreSync?.startStateFirebaseSync(object : FirestoreResponseCompletionHandler {
                override fun onSuccess(result: Any?, observerType: FirebaseObserverType) {
                    swipe_container?.isRefreshing = false
                    var state = mapStateData(result as State)
                    when (observerType) {
                        FirebaseObserverType.CHILD_ADDED -> {
                            if (state.name == "India") {
                                setupTopCard(state)
                            } else {
                                list.add(state)
                                reloadData()
                            }
                        }

                        FirebaseObserverType.CHILD_CHANGED -> {
                            if (state.name == "India") {
                                setupTopCard(state)
                            } else {
                                val iterator: MutableListIterator<State> = list.listIterator()
                                var index = 0;
                                while (iterator.hasNext()) {
                                    if (iterator.next().name == state.name) {
                                        iterator.set(state)
                                        recyclerViewAdapter.notifyItemChanged(index)
                                        break
                                    }
                                    index++
                                }
                            }
                        }

                        FirebaseObserverType.CHILD_REMOVED -> {
                            val iterator :MutableListIterator<State> = list.listIterator()
                            var index = 0
                            while (iterator.hasNext()) {
                                if (iterator.next().name == state.name) {
                                    iterator.remove()
                                    recyclerViewAdapter.notifyItemRemoved(index)
                                    break
                                }
                                index++
                            }
                        }
                    }
                }

                override fun onFailure(result: Any?) {
                    reloadData()
                }

            })
        }
    }

    private fun reloadData() {
        if (list.size > 0) {
            recycler_view?.visibility = View.VISIBLE
            recycler_view?.smoothScrollToPosition(0)
            recyclerViewAdapter?.setData(list)
        } else {
            recycler_view?.visibility = View.GONE
            recyclerViewAdapter?.notifyDataSetChanged()
        }
    }

    private fun mapStateData(s: State): State {
        if (s.name != "India") {
            var state = s
            if (state.increment.containsKey("Confirmed")) {
                state.daily_confirmed = state.increment["Confirmed"] ?: 0
                state.daily_deceased = state.increment["Deceased"] ?: 0
                state.daily_confirmed = state.increment["Recovered"] ?: 0
            }
            if (state.districtData.isNotEmpty()) {
                val iterator: MutableListIterator<District> = state.districtData.listIterator()
                while (iterator.hasNext()) {
                    val district = iterator.next().copy()
                    district.confirmed = district.data.get("confirmed").toString().toInt()
                    iterator.set(district)
                }
                state.districtData.sortBy { -it.confirmed }
            }
            return state
        }
        return s;
    }

    private fun restartSync() {
        setupFilterText()
        statesFirestoreSync?.stopFirestoreSync()
        statesFirestoreSync = null
        startStateFirebaseSync()
    }

    companion object {
        fun newInstance() =
            DataListFragment()
    }

    override fun onItemTapped(state: State) {
        startActivity(StateDetailActivity.newIntent(context!!, state))
    }

    override fun onRefresh() {
      restartSync()
    }

}
