package com.example.jgg.dataManagers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.jgg.Champion
import com.example.jgg.Database
import com.example.jgg.InputActivity
import com.example.jgg.Skill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.reflect.KSuspendFunction1

class NetworkManager constructor(val inputActivity: InputActivity)
{
    //Codigo para hacer la clase Singleton

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: NetworkManager? = null
        fun getInstance(inputActivity: InputActivity) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkManager(inputActivity).also {
                    INSTANCE = it
                }
            }

        fun stringToArray(string: String) : Array<String>
        {

            var final = mutableListOf<String>()
            var lastindex = 0

            for(i in string.indices)
            {
                if(string[i] == '*')
                {
                    var s = getString(lastindex, i, string)
                    final.add(s)
                    lastindex = i+1
                }
            }

            return  final.toTypedArray()



        }


        fun getString(from: Int, to: Int, original: String) : String
        {
            var final = ""

            for( i in from until  to)
            {
                final += original[i]
            }

            return  final



        }

    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(inputActivity.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }

    //Codigo de la query ( no editar si es posible)

    public suspend fun query(
        url: String,
        onValidexternalFunction: KSuspendFunction1<String, Unit>,
        onInvalidexternalFunction: KSuspendFunction1<String, Unit>,
        context: Context
    )
    {
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                GlobalScope.launch(Dispatchers.Main) {
                    onValidexternalFunction(response.toString())

                }
            },
            { error ->
                GlobalScope.launch(Dispatchers.Main) {
                    onInvalidexternalFunction(error.toString())
                }

            }
        )

        // Access the RequestQueue through your singleton class.
        getInstance(inputActivity).addToRequestQueue(jsonObjectRequest)


    }


    //CustomCode (a partir de aqui toodo lo que sea libre)
    public  suspend fun manageNetworkError(msg: String)
    {
        Log.d("Custom", msg);
    }

    public suspend fun queryAllChampionsInfo()
    {

        if(inputActivity.db.ChampionDAO().getAllChampionsNames().isEmpty()) // get info from NW then, display
        {
            val url = "https://ddragon.leagueoflegends.com/cdn/11.12.1/data/en_US/champion.json"
            query(url, ::insertAllChampionsInfo, ::manageNetworkError, inputActivity)
        }
        else // display from db
        {
            var champs = inputActivity.db.ChampionDAO().getAllChampions()
            inputActivity.runOnUiThread(Runnable {
                inputActivity.view.updateVisuals(champs)
            })
        }

    }



    fun arrayToString(array: Array<String>) : String
    {
        var final = ""

        for(s in array)
        {
            final+= s+"*"
        }

        return  final
    }


    suspend fun getChampionIndividualJSON(champID : String){
        var url = "https://ddragon.leagueoflegends.com/cdn/11.12.1/data/en_US/champion/$champID.json"
        query(url, ::parseIndividualChampionInfo, ::manageNetworkError, inputActivity)
    }

    public suspend fun parseIndividualChampionInfo(response: String){
        val fulljson = JSONObject(response)
        val data = fulljson.getJSONObject("data")
        val champion = data.getJSONObject(data.names()[0].toString())
        val championid = champion.getString("id")

        val spells = champion.getJSONArray("spells")

        for(i in 0 until spells.length()){
            val skill = spells.getJSONObject(i)

            val skillName = skill.getString("name")
            val skillDescription = skill.getString("description")

            val skillImageName = skill.getJSONObject("image").getString("full")
            val skillImageURL = "https://ddragon.leagueoflegends.com/cdn/11.12.1/img/spell/$skillImageName"

            val s = Skill(
                    0,
                    skillName,
                    skillDescription,
                    skillImageURL,
                    championid
            )

            inputActivity.db.SkillDAO().insert(s)
        }

        val lore = champion.getString("lore")
        inputActivity.db.ChampionDAO().updateLore(championid,lore)



        val champ = inputActivity.db.ChampionDAO().getChampion(championid)
        val skills = ArrayList(inputActivity.db.SkillDAO().getAllSkillsFromChampion(championid))
        inputActivity.model.startThirdActivity(champ,skills)


    }

    public suspend fun insertAllChampionsInfo(response: String)
    {
        val fulljson =  JSONObject(response)
        val data = fulljson.getJSONObject("data");
        val championNames = data.names()

        for (i in 0 until data.length()){
            val champion = data.getJSONObject(championNames[i].toString())

            val id = champion.getString("id")

            val champSquarePortraitURL = "https://ddragon.leagueoflegends.com/cdn/11.12.1/img/champion/$id.png"

            val name = champion.getString("name")
            val title = champion.getString("title")

            val champStats = champion.getJSONObject("info")

            val attackValue = champStats.getInt("attack")
            val defenseValue = champStats.getInt("defense")
            val magicValue = champStats.getInt("magic")
            val difficultyValue = champStats.getInt("difficulty")

            val champTags = champion.getJSONArray("tags")

            var champTagsArray: Array<String> = Array(champTags.length()) { "" }

            for (i in 0 until champTags.length()) {
                champTagsArray[i] = champTags.getString(i).toString()
            }



            val c = Champion(
                    0,
                    id,
                    name,
                    title,
                    attackValue,
                    defenseValue,
                    magicValue,
                    difficultyValue,
                    arrayToString(champTagsArray),
                    champSquarePortraitURL,"empty"
            )
            inputActivity.db.ChampionDAO().insert(c) // se mete en la DB
            //getChampionIndividualJSON(id)
        }



        var champs = inputActivity.db.ChampionDAO().getAllChampions()

        inputActivity.runOnUiThread(Runnable {
            inputActivity.view.updateVisuals(champs)
        })




    }




//al pasar a la segunda act, comprobar si esta el lore, si no esta bloquear carga y enseñar mensaje de: aun estamos buscando



}