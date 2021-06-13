package com.example.covidstats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jgg.Champion
import com.example.jgg.dataManagers.ChampionData

@Dao
interface ChampionDAO
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(champion: Champion)

    @Query("SELECT * FROM champion_table")
    suspend fun getAllChampions(): List<Champion>

    @Query("SELECT name FROM Champion_table")
    suspend fun getAllChampionsNames(): List<String>



}