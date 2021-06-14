package com.example.jgg

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.jgg.dataManagers.NetworkManager

class DisplayAcitvity : AppCompatActivity() {

    lateinit var champion: Champion
    lateinit var  skills: ArrayList<Skill>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_acitvity)

        var intentData =  intent.getSerializableExtra("champion") as? Champion
        intentData?.let { champion = it }

        var intentData2 =  intent.getSerializableExtra("skills") as? ArrayList<Skill>
        intentData2?.let { skills = it }

        updateScreen()

    }

    fun updateScreen()
    {
        Glide.with(this).load(champion.portraitURL).into(findViewById(R.id.Portrait))

        findViewById<TextView>(R.id.Name).text = champion.name
        findViewById<TextView>(R.id.Title).text = champion.title

        findViewById<TextView>(R.id.attackValue).text = champion.attackValue.toString()
        findViewById<TextView>(R.id.defenseValue).text = champion.defenseValue.toString()
        findViewById<TextView>(R.id.magicValue).text = champion.magicValue.toString()
        findViewById<TextView>(R.id.difficultyValue).text = champion.difficultyValue.toString()

        displayTags()

        //abilities
        Glide.with(this).load(skills[0].skillImageURL).placeholder(R.drawable.loading).into(findViewById<ImageView>(R.id.ab1))
        Glide.with(this).load(skills[1].skillImageURL).placeholder(R.drawable.loading).into(findViewById<ImageView>(R.id.ab2))
        Glide.with(this).load(skills[2].skillImageURL).placeholder(R.drawable.loading).into(findViewById<ImageView>(R.id.ab3))
        Glide.with(this).load(skills[3].skillImageURL).placeholder(R.drawable.loading).into(findViewById<ImageView>(R.id.ab4))




    }

    fun displayTags()
    {
        var array = NetworkManager.stringToArray(champion.arrayOfTags)

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

        findViewById<TextView>(R.id.TagsTitle).text = msg
    }

    public fun displayLore(view: View)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(champion.name + "'s lore")
        builder.setMessage(champion.lore)


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
            R.id.ab1 ->{abilityName = skills[0].name ; abilityDescription = skills[0].skillDescription}
            R.id.ab2 ->{abilityName = skills[1].name ; abilityDescription = skills[1].skillDescription}
            R.id.ab3 -> {abilityName = skills[2].name ; abilityDescription = skills[2].skillDescription}
            R.id.ab4 -> {abilityName = skills[3].name ; abilityDescription = skills[3].skillDescription}
            else -> { // Note the block
                Log.d("Custom","Error")
            }
        }




        val builder = AlertDialog.Builder(this)
        builder.setTitle(abilityName)
        builder.setMessage(abilityDescription)


        builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
            //Button Action
        }
        builder.show()
    }
}