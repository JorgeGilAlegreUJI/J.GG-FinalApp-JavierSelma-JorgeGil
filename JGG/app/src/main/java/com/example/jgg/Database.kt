package com.example.jgg

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.jgg.ChampionDAO
import com.example.jgg.SkillDAO
import com.example.jgg.Champion

@Database(entities = [Champion::class, Skill::class],version =  2,exportSchema = false)
abstract  class Database : RoomDatabase()
{
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: com.example.jgg.Database? = null

        fun getInstance(context: Context): com.example.jgg.Database {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): com.example.jgg.Database {
            return Room.databaseBuilder(context, com.example.jgg.Database::class.java, "DATABASE")
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract  fun ChampionDAO(): ChampionDAO
    abstract  fun SkillDAO(): SkillDAO
}