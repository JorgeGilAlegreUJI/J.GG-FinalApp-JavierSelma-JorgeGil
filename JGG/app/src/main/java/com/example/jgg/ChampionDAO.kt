package com.example.jgg

import androidx.room.*

@Dao
interface ChampionDAO
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(champion: Champion)

    @Query("UPDATE Champion_table SET lore =:lore WHERE championID=:championID")
    suspend fun updateLore(championID:String,lore:String)

    @Query("SELECT * FROM champion_table WHERE championID=:champID")
    suspend fun getChampion(champID:String): Champion

    @Query("SELECT * FROM champion_table")
    suspend fun getAllChampions(): List<Champion>

    @Query("SELECT name FROM Champion_table")
    suspend fun getAllChampionsNames(): List<String>

}