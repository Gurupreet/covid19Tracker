package com.guru.covid19tracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "main_table")
data class ApiData(
    @PrimaryKey val name: String
)