package com.guru.covid19tracker.utils

enum class Filter(name: String) {
    CONFIRMED("confirmed"),
    ACTIVE("active"),
    RECOVERED("recovered"),
    DEATHS("deaths"),
    DECEASED("Deceased"),
    INCREMENT("increment")
}