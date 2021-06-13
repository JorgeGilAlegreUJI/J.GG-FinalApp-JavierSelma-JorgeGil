package com.example.jgg

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Champion_table")
data class Champion
    (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:String,
    val title:String,
    val attackValue:Int,
    val defenseValue:Int,
    val magicValue:Int,
    val difficultyValue:Int,
    val arrayOfTags:String,
    val portraitURL:String
)
{
    override fun toString(): String {
        return name
    }
}
