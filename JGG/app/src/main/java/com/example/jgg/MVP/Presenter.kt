package com.example.jgg.MVP

import android.content.Context
import android.content.Intent
import com.example.jgg.Champion
import com.example.jgg.DisplayAcitvity
import com.example.jgg.InputActivity
import com.example.jgg.Skill

class Presenter (val context: Context)
{
    public  fun startSecondActivity()
    {
        val intent = Intent(context, InputActivity::class.java)
        context.startActivity(intent)
    }

    public fun startThirdActivity(champion: Champion, skills: ArrayList<Skill>)
    {
        if(champion.lore == "empty")return // show message (?)

        val intent = Intent(context, DisplayAcitvity::class.java).apply{
            putExtra("champion", champion).putExtra("skills",skills)
        }

        if(context is InputActivity) context.activeSnackbar.dismiss()
        context.startActivity(intent)
    }


}