package com.example.jgg.dataManagers

import android.graphics.Bitmap
import android.media.Image

//añadir las demás variables que hagan falta
data class ChampionData(val name:String,val title:String,
                        val attackValue:Int, val defenseValue:Int, val magicValue:Int, val difficultyValue:Int,
                        val arrayOfTags:Array<String>, val portraitURL:String)