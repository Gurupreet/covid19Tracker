package com.guru.covid19tracker.models

data class News(val message_text: String,
                val sender_name: String,
                val source: String,
                val attachment_url: String,
                val avatar_url: String,
                val first_paragraph: String,
                val video_url: String,
                val id: String,
                val created_at: Long) {
    constructor(): this("","", "", "", "", "", "", "", 0)
}