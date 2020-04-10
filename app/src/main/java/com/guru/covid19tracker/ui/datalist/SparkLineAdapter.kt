package com.guru.covid19tracker.ui.datalist

import android.graphics.RectF
import com.robinhood.spark.SparkAdapter
import java.util.ArrayList

class SparkLineAdapter(val data: ArrayList<Float>, val confirmed: Int) :
    SparkAdapter() {
    private var yData = ArrayList<Float>()

    fun updateData(newData: ArrayList<Float>) {
        if (yData.size < newData.size) {
            yData.clear()
            yData.addAll(newData)
            notifyDataSetChanged()
        }
    }

    override fun hasBaseLine(): Boolean {
        return true
    }

    override fun getBaseLine(): Float {
        return 0f
    }

    override fun getCount(): Int {
        return yData.size
    }
    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getDataBounds(): RectF {
        return RectF(0f, 0f, yData.size.toFloat(), confirmed.toFloat())
    }
    override fun getY(index: Int): Float {
        return yData[index]
    }

    init {
        yData = data
    }
}