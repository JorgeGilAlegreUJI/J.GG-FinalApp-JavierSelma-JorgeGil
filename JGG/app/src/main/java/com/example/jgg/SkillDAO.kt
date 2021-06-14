package com.example.jgg

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jgg.Skill

@Dao
interface SkillDAO
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(skill : Skill)

    @Query("SELECT * FROM Skill_table WHERE champion_id=:champion_id")
    suspend fun getAllSkillsFromChampion(champion_id : String): List<Skill>

    @Query("SELECT name FROM Skill_table WHERE champion_id=:champion_id")
    suspend fun getAllChampionsNames(champion_id: String): List<String>



}