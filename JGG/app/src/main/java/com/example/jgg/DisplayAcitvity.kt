package com.example.jgg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.jgg.dataManagers.ChampionData

class DisplayAcitvity : AppCompatActivity() {

    lateinit var championData : ChampionData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_acitvity)

        val intentData =  intent.getSerializableExtra(EXTRA_MESSAGE) as? ChampionData
        intentData?.let { championData = it }

        updateScreen()

    }

    fun updateScreen()
    {
        Glide.with(this).load(championData.portraitURL).into(findViewById(R.id.Portrait))

        findViewById<TextView>(R.id.Name).text = championData.name
        findViewById<TextView>(R.id.Title).text = championData.title

        findViewById<TextView>(R.id.attackValue).text = championData.attackValue.toString()
        findViewById<TextView>(R.id.defenseValue).text = championData.defenseValue.toString()
        findViewById<TextView>(R.id.magicValue).text = championData.magicValue.toString()
        findViewById<TextView>(R.id.difficultyValue).text = championData.difficultyValue.toString()

        var msg = ""
        for(i in championData.arrayOfTags.indices)
        {
            if(championData.arrayOfTags.size >= 2)
            {
                if(i == championData.arrayOfTags.size-1)
                {
                    msg += " & "
                }
            }

            var s = championData.arrayOfTags[i]
            msg += s


        }

        findViewById<TextView>(R.id.tags).text = msg


    }
}