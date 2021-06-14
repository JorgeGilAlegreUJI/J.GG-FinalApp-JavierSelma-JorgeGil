package com.example.jgg

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.covidstats.RecyclerAdapter
import com.example.jgg.MVP.Model
import com.example.jgg.MVP.View
import com.example.jgg.dataManagers.NetworkManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class InputActivity : AppCompatActivity()
{
    public val networkManager = NetworkManager.getInstance(this)
    public lateinit var db : Database
    public lateinit var RV : RecyclerView
    public  lateinit var activeSnackbar: Snackbar
    public  val view : View = View(this)
    public  val model:Model = Model(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)
        RV =  findViewById<RecyclerView>(R.id.rv_recyclerView)
        db = Database.getInstance(this)


        GlobalScope.launch {
            var champs = db.ChampionDAO().getAllChampions()

            if(champs.isEmpty()) // si no hay datos en la database los saca de internet
            {
                model.loadFromNetwork()
            }
            else // si hay datos en la database simplemente los extrae y los carga
            {
                model.loadFromDatabase(champs)
            }

        }


    }



}