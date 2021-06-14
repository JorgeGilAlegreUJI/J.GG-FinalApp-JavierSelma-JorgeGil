package com.example.covidstats
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.jgg.Champion
import com.example.jgg.InputActivity
import com.example.jgg.MVP.Presenter
import com.example.jgg.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RecyclerAdapter( private  var ChampionsData : List<Champion>,private  var inputActivity: InputActivity) :
        RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageView : ImageView = itemView.findViewById(R.id.image)
        val textView : TextView = itemView.findViewById(R.id.NameTextView)

        //takes care of click events
        init {


            itemView.setOnClickListener { v: View ->
                //CLick on element





                GlobalScope.launch {
                    var champID = ChampionsData[adapterPosition].championID
                    var lore = inputActivity.db.ChampionDAO().getChampion(champID).lore

                    if(lore =="empty")
                    {
                        inputActivity.activeSnackbar =  inputActivity.view.showSnackBar("Retrieving data from Network, please wait")
                        inputActivity.networkManager.getChampionIndividualJSON(ChampionsData[adapterPosition].championID)
                    }
                    else
                    {
                        inputActivity.activeSnackbar =  inputActivity.view.showSnackBar("Retrieving data from Database, please wait")
                        val champ = inputActivity.db.ChampionDAO().getChampion(champID)
                        val skills = ArrayList(inputActivity.db.SkillDAO().getAllSkillsFromChampion(champID))
                        val p = Presenter(inputActivity)
                        p.startThirdActivity(champ,skills)

                    }



                    //var skills =  inputActivity.db.SkillDAO().getAllSkillsFromChampion(ChampionsData[adapterPosition].championID)

                    //inputActivity.startSecondActivity(ChampionsData[adapterPosition], ArrayList(skills))
                }


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


        Glide.with(inputActivity).load(ChampionsData[position].portraitURL).placeholder(R.drawable.loading).into(holder.imageView)


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

        inputActivity.activeSnackbar.dismiss()

    }




}
