package com.example.jgg

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.covidstats.RecyclerAdapter


class InputActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        updateRV()

    }


    private fun updateRV()
    {
        var rV = findViewById<RecyclerView>(R.id.rv_recyclerView)
        rV.layoutManager = GridLayoutManager(this, 5)
        var images = mutableListOf<String>()

        for( i in 0 until  200)
        {
            images.add("https://ddragon.leagueoflegends.com/cdn/11.12.1/img/champion/Aatrox.png")
        }

        rV.adapter = RecyclerAdapter(images, this)


    }


}