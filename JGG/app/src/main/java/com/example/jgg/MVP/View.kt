package com.example.jgg.MVP

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.covidstats.RecyclerAdapter
import com.example.jgg.Champion
import com.example.jgg.DisplayAcitvity
import com.example.jgg.InputActivity
import com.example.jgg.R
import com.example.jgg.dataManagers.NetworkManager
import com.google.android.material.snackbar.Snackbar

class View (val inputActivity: InputActivity)
{
    public fun showSnackBar(msg: String) : Snackbar
    {
        var s = Snackbar.make(
                inputActivity.findViewById(R.id.myLayout),
                msg,
                Snackbar.LENGTH_SHORT
        )

        s.show()

        return  s
    }

    public fun updateVisuals(champions: List<Champion>)
    {
        var spanCount = 4

        if (inputActivity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) spanCount = 7

        inputActivity.runOnUiThread(Runnable {
            inputActivity.RV.layoutManager = GridLayoutManager(inputActivity, spanCount)
            inputActivity.RV.adapter = RecyclerAdapter(champions, inputActivity)
        })


    }

    public  fun updateChamps(champs: List<Champion>)
    {
        inputActivity.runOnUiThread(Runnable {
            updateVisuals(champs)
        })
    }

}

class DisplayView(val displayAcitvity: DisplayAcitvity)
{
    fun updateScreen()
    {
        Glide.with(displayAcitvity).load(displayAcitvity.champion.portraitURL).into(displayAcitvity.findViewById(R.id.Portrait))

        displayAcitvity.findViewById<TextView>(R.id.Name).text = displayAcitvity.champion.name
        displayAcitvity.findViewById<TextView>(R.id.Title).text = displayAcitvity.champion.title

        displayAcitvity.findViewById<TextView>(R.id.attackValue).text = displayAcitvity.champion.attackValue.toString()
        displayAcitvity.findViewById<TextView>(R.id.defenseValue).text = displayAcitvity.champion.defenseValue.toString()
        displayAcitvity.findViewById<TextView>(R.id.magicValue).text = displayAcitvity.champion.magicValue.toString()
        displayAcitvity.findViewById<TextView>(R.id.difficultyValue).text = displayAcitvity.champion.difficultyValue.toString()

        displayTags()

        //abilities
        Glide.with(displayAcitvity).load(displayAcitvity.skills[0].skillImageURL).placeholder(R.drawable.loading).into(displayAcitvity.findViewById<ImageView>(R.id.ab1))
        Glide.with(displayAcitvity).load(displayAcitvity.skills[1].skillImageURL).placeholder(R.drawable.loading).into(displayAcitvity.findViewById<ImageView>(R.id.ab2))
        Glide.with(displayAcitvity).load(displayAcitvity.skills[2].skillImageURL).placeholder(R.drawable.loading).into(displayAcitvity.findViewById<ImageView>(R.id.ab3))
        Glide.with(displayAcitvity).load(displayAcitvity.skills[3].skillImageURL).placeholder(R.drawable.loading).into(displayAcitvity.findViewById<ImageView>(R.id.ab4))


    }

    private fun displayTags()
    {
        var array = NetworkManager.stringToArray(displayAcitvity.champion.arrayOfTags)

        var msg = ""
        for(i in array.indices)
        {
            if(array.size >= 2)
            {
                if(i == array.size-1)
                {
                    msg += " & "
                }
            }

            var s = array[i]
            msg += s


        }

        displayAcitvity.findViewById<TextView>(R.id.TagsTitle).text = msg
    }

    public fun displayLore(view: View)
    {
        val builder = AlertDialog.Builder(displayAcitvity)
        builder.setTitle(displayAcitvity.champion.name + "'s lore")
        builder.setMessage(displayAcitvity.champion.lore)


        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
            //Button Action
        }
        builder.show()
    }

    public fun displayAbilityInfo(view: View)
    {
        var abilityName : String = ""
        var abilityDescription: String =  ""

        when (view.id) {
            R.id.ab1 ->{abilityName = displayAcitvity.skills[0].name ; abilityDescription = displayAcitvity.skills[0].skillDescription}
            R.id.ab2 ->{abilityName = displayAcitvity.skills[1].name ; abilityDescription = displayAcitvity.skills[1].skillDescription}
            R.id.ab3 -> {abilityName = displayAcitvity.skills[2].name ; abilityDescription = displayAcitvity.skills[2].skillDescription}
            R.id.ab4 -> {abilityName = displayAcitvity.skills[3].name ; abilityDescription = displayAcitvity.skills[3].skillDescription}
            else -> { // Note the block
                Log.d("Custom","Error")
            }
        }




        val builder = AlertDialog.Builder(displayAcitvity)
        builder.setTitle(abilityName)
        builder.setMessage(abilityDescription)


        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
            //Button Action
        }
        builder.show()
    }

}