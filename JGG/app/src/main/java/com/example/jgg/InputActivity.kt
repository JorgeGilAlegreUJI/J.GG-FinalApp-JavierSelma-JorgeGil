package com.example.jgg

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidstats.RecyclerAdapter
import com.example.jgg.dataManagers.ChampionData
import com.example.jgg.dataManagers.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InputActivity : AppCompatActivity()
{
    private val networkManager = NetworkManager.getInstance(this)
    public lateinit var db : Database
    public lateinit var RV : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        RV =  findViewById<RecyclerView>(R.id.rv_recyclerView)
        db = Database.getInstance(this)
        if(networkManager.AllChampionData.isEmpty())makeQuery()
        else updateVisuals()
    }

    fun makeQuery()
    {
        GlobalScope.launch {
            networkManager.queryAllChampionsInfo()

        }
    }

    public fun updateVisuals()
    {
        var spanCount = 4

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) spanCount = 7


        RV.layoutManager = GridLayoutManager(this, spanCount)
        RV.adapter = RecyclerAdapter(networkManager.AllChampionData, this)
    }


    fun startSecondActivity(championData: ChampionData)
    {

        val intent = Intent(this, DisplayAcitvity::class.java).apply{
            putExtra(EXTRA_MESSAGE, championData)
        }

        this.startActivity(intent)
    }




}