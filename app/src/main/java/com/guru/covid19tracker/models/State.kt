package com.guru.covid19tracker.models

import java.io.Serializable

data class State(
    val name: String = "",
    val extra: String = "",
    val confirmed: Int,
    val recovered: Int,
    val deaths: Int,
    var active: Int,
    var increment: Map<String, Int>,
    var daily_confirmed: Int,
    var daily_deceased: Int,
    var daily_recovered: Int,
    var updatedAt: Long,
    var districtData: MutableList<District>,
    val confirmed_history: ArrayList<Float>,
    val death_history: ArrayList<Float>,
    val active_history: ArrayList<Float>,
    val recovered_history: ArrayList<Float>): Serializable {
    constructor() : this("", "", 0, 0, 0, 0, mapOf<String, Int>(), 0,0,0,0, arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf(), arrayListOf())
}


data class District(val name: String,
                    val data: HashMap<String, Any>,
                    var confirmed: Int): Serializable {
    constructor(): this("", HashMap<String,Any>(), 0)
}