package com.example.jgg.MVP

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.covidstats.RecyclerAdapter
import com.example.jgg.Champion
import com.example.jgg.InputActivity
import com.example.jgg.R
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