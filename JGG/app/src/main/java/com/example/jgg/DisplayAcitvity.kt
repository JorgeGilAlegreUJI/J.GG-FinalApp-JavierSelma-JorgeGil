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
import com.example.jgg.MVP.DisplayView
import com.example.jgg.dataManagers.NetworkManager

class DisplayAcitvity : AppCompatActivity() {

    public lateinit var champion: Champion
    public lateinit var  skills: ArrayList<Skill>
    val view : DisplayView = DisplayView(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_acitvity)

        var intentData =  intent.getSerializableExtra("champion") as? Champion
        intentData?.let { champion = it }

        var intentData2 =  intent.getSerializableExtra("skills") as? ArrayList<Skill>
        intentData2?.let { skills = it }

        view.updateScreen()

    }

    public fun displayLore(view: View)
    {
        this.view.displayLore(view)
    }

    public fun displayAbilityInfo(view: View)
    {
        this.view.displayAbilityInfo(view)
    }
}