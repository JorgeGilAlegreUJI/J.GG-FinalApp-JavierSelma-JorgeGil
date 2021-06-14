package com.example.jgg.dataManagers

import android.graphics.Bitmap
import android.media.Image
import java.io.Serializable

//añadir las demás variables que hagan falta
data class ChampionData(val name:String,val title:String, val championID:String,
                        val attackValue:Int, val defenseValue:Int, val magicValue:Int, val difficultyValue:Int,
                        val arrayOfTags:Array<String>, val portraitURL:String) : Serializable