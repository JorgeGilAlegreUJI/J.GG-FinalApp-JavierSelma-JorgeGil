package com.example.jgg

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(tableName = "Skill_table",
        foreignKeys = [ForeignKey(entity = Champion::class,
        parentColumns = arrayOf("championID"),
        childColumns = arrayOf("champion_id"),
        onDelete = ForeignKey.CASCADE)])
data class Skill
(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        val name:String,
        val skillDescription:String,
        val skillImageURL:String,
        @ColumnInfo(name = "champion_id", index = true)
        var champion_id: String
)
{
    override fun toString(): String {
        return name
    }
}
