package com.example.jgg.MVP

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.jgg.Champion
import com.example.jgg.DisplayAcitvity
import com.example.jgg.InputActivity
import com.example.jgg.Skill

class Model (private val inputActivity: InputActivity)
{
    public fun startThirdActivity(champion: Champion, skills: ArrayList<Skill>)
    {
        if(champion.lore == "empty")return // show message (?)

        val intent = Intent(inputActivity, DisplayAcitvity::class.java).apply{
            putExtra("champion", champion).putExtra("skills",skills)
        }

        inputActivity.activeSnackbar.dismiss()
        inputActivity.startActivity(intent)
    }

    public suspend  fun loadFromNetwork()
    {
        inputActivity.activeSnackbar =  inputActivity.view.showSnackBar("Retrieving data from Network, please wait.")
        inputActivity.networkManager.queryAllChampionsInfo()
    }

    public suspend fun loadFromDatabase(champs : List<Champion>)
    {
        inputActivity.activeSnackbar =  inputActivity.view.showSnackBar("Retrieving data from Database, please wait.")
        inputActivity.view.updateVisuals(champs)
    }




}