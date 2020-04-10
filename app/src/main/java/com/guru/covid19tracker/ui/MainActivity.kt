package com.guru.covid19tracker.ui

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.guru.covid19tracker.R
import com.guru.covid19tracker.ui.datalist.DataListFragment
import com.guru.covid19tracker.ui.info.InfoFragment
import com.guru.covid19tracker.ui.news.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.guru.covid19tracker.data.firebase.FirebaseManager
import com.guru.covid19tracker.utils.Naviation
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var TAG = Naviation.CASES
    private val mainViewModel: MainViewModel by viewModel()

    private var mDataListFragment: DataListFragment? = null
    private var mInfoFragment: InfoFragment? = null
    private var mNewsFragment: NewsFragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseManager.initInsanceID()
        handler = Handler()
        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment(TAG)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.list -> {
                TAG = Naviation.CASES
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.news -> {
                TAG = Naviation.NEWS
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
            R.id.alerts -> {
                TAG = Naviation.INFO
                loadFragment(TAG)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadFragment(type : Naviation) {
        val fragment  = when (type) {
            Naviation.CASES -> {
                if (mDataListFragment == null) {
                    mDataListFragment = DataListFragment.newInstance()
                }
                mDataListFragment
            }
            Naviation.NEWS ->  {
                if (mNewsFragment == null) {
                        mNewsFragment = NewsFragment.newInstance()
                }
                mNewsFragment
            }
            Naviation.INFO -> {
                if (mInfoFragment == null) {
                    mInfoFragment = InfoFragment.newInstance()
                }
                mInfoFragment
            }
        }
        handler?.post {
            try {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.container, fragment as Fragment)
                fragmentTransaction.commit()
            } catch (e: IllegalArgumentException) {
                Toast.makeText(this, "Error loading fragments", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
