package com.example.jgg

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.jgg.dataManagers.ChampionData
import com.example.jgg.dataManagers.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InputActivity : AppCompatActivity()
{
    private val networkManager = NetworkManager.getInstance(this)
    public lateinit var RV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        RV =  findViewById<RecyclerView>(R.id.rv_recyclerView)
        updateScreen()
    }

    override fun onResume() {
        super.onResume()
        updateScreen()

    }

    fun updateScreen()
    {
        GlobalScope.launch {
            networkManager.queryAllChampionsInfo()

        }
    }


    fun startSecondActivity(championData: ChampionData)
    {

        val intent = Intent(this, DisplayAcitvity::class.java).apply{
            putExtra(EXTRA_MESSAGE, championData)
        }

        this.startActivity(intent)
    }


}