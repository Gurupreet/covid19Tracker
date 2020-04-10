package com.guru.covid19tracker.ui.statedeatils

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.guru.covid19tracker.R
import com.guru.covid19tracker.models.State
import kotlinx.android.synthetic.main.activity_state_detail.*

class StateDetailActivity : AppCompatActivity() {

    lateinit var state: State
    lateinit var stateDetailAdapter: StateDetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_detail)
        state = intent.getSerializableExtra(STATE_PARAM) as State
        setupToolbar()
        setupTopCard()
        setupRecyclerView()
    }

    fun setupToolbar() {
        state.apply {
            toolbar.title = name
        }
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setupTopCard() {
            new_cases.text = "New cases: +${state.daily_confirmed}, New deaths: +${state.daily_deceased}"
            confirmed.text = state.confirmed.toString()
            active.text = state.active.toString()
            recovered.text = state.recovered.toString()
            deceased.text = state.deaths.toString()

    }
    private fun setupRecyclerView() {
        stateDetailAdapter =
            StateDetailAdapter()
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = stateDetailAdapter
        stateDetailAdapter.setData(state.districtData)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        const val STATE_PARAM = "state"
        fun newIntent(context: Context, state: State) = Intent(context, StateDetailActivity::class.java).apply {
            putExtra(STATE_PARAM,  state)
        }
    }
}
