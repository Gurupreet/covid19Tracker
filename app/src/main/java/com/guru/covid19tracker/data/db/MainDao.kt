package com.guru.covid19tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.guru.covid19tracker.models.ApiData

@Dao
interface MainDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertApiData(apiDataList: List<ApiData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addApiData(apiData: ApiData)

    @Transaction
    @Query("select * from main_table")
    fun getApiDataFromDatabase(): LiveData<List<ApiData>>

    @Query("DELETE FROM main_table")
    fun deleteAll()

}