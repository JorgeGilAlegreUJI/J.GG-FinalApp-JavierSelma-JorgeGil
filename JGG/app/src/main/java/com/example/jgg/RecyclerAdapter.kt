package com.example.covidstats
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.jgg.InputActivity
import com.example.jgg.R
import org.w3c.dom.Text

class RecyclerAdapter( private  var imagesurl : MutableList<String>,private  var inputActivity: InputActivity) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ImageView = itemView.findViewById(R.id.image)

        //takes care of click events
        init {
            imageView.setImageResource(R.drawable.aatrox)


            itemView.setOnClickListener { v: View ->
                //CLick on element
            }
        }


    }


    override fun getItemCount(): Int {
        return imagesurl.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        Glide.with(inputActivity).load("http://goo.gl/gEgYUd").into(holder.imageView);
    }


}
