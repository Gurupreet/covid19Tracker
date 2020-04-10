package com.guru.covid19tracker.models

data class Info (
    val title: String,
    val content: String,
    val content2: String ="",
    val content3: String =""
) {
    constructor(): this("", "" ,"", "")
}