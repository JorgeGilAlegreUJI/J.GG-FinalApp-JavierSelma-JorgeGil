package com.example.jgg

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.jgg.MVP.Model
import com.example.jgg.MVP.Presenter
import com.example.jgg.dataManagers.NetworkManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.URL


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    public fun startActivity(view: View)
    {
        Presenter(this).startSecondActivity()
    }




}

