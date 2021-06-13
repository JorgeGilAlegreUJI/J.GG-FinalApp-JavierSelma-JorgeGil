package com.example.covidstats
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.jgg.InputActivity
import com.example.jgg.R
import com.example.jgg.dataManagers.ChampionData
import org.w3c.dom.Text
import java.sql.Time

class RecyclerAdapter( private  var ChampionsData : MutableList<ChampionData>,private  var inputActivity: InputActivity) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ImageView = itemView.findViewById(R.id.image)
        val textView : TextView = itemView.findViewById(R.id.NameTextView)

        //takes care of click events
        init {


            itemView.setOnClickListener { v: View ->
                //CLick on element
                inputActivity.startSecondActivity(ChampionsData[adapterPosition])
            }
        }


    }


    override fun getItemCount(): Int {
        return ChampionsData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val champName = ChampionsData[position].name


        Glide.with(inputActivity).load(ChampionsData[position].portraitURL).into(holder.imageView)


        holder.textView.text = champName

        //tamaño texto
        val maxSize = 15.5f
        val minSize = 8f
        if(champName.length > 6)
        {
            var proportion = (maxSize - minSize) / (14f-7f) // los literales son la largaria de la cadena del nombre ( max y min)
            var incremento = (champName.length - minSize)*proportion
            var finalSize = maxSize- incremento
            holder.textView.textSize = finalSize
        }
        else
        {
            holder.textView.textSize = maxSize
        }

    }




}
