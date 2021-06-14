package com.example.jgg.dataManagers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.jgg.Champion
import com.example.jgg.InputActivity
import com.example.jgg.Skill
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.reflect.KSuspendFunction1

class NetworkManager constructor(val inputActivity: InputActivity)
{
    public val AllChampionData = mutableListOf<ChampionData>()
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
        AllChampionData.clear()

        if(inputActivity.db.ChampionDAO().getAllChampionsNames().isEmpty())
        {
            val url = "https://ddragon.leagueoflegends.com/cdn/11.12.1/data/en_US/champion.json"
            query(url, ::insertAllChampionsInfo, ::manageNetworkError, inputActivity)
        }
        else
        {
            insertFromDBonList()
        }

    }

    public  suspend fun insertFromDBonList()
    {
        var champs = inputActivity.db.ChampionDAO().getAllChampions()

        for(c in champs)
        {
            var data = ChampionData(
                c.name,
                c.title,
                    c.championID,
                c.attackValue,
                c.defenseValue,
                c.magicValue,
                c.difficultyValue,
                stringToArray(
                    c.arrayOfTags
                ),
                c.portraitURL
            )
            AllChampionData.add(data)
        }

        inputActivity.runOnUiThread(Runnable {
            inputActivity.updateVisuals()
        })


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

    fun arrayToString(array: Array<String>) : String
    {
        var final = ""

        for(s in array)
        {
            final+= s+"*"
        }

        return  final
    }

    suspend  fun addChampToDatabase(championData: ChampionData)
    {
        val c = Champion(
            0,
                championData.championID,
            championData.name,
            championData.title,
            championData.attackValue,
            championData.defenseValue,
            championData.magicValue,
            championData.difficultyValue,
            arrayToString(
                championData.arrayOfTags
            ),
            championData.portraitURL,
        )
        inputActivity.db.ChampionDAO().insert(c)
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




            val championdata = ChampionData(
                name,
                title,
                    id,
                attackValue,
                defenseValue,
                magicValue,
                difficultyValue,
                champTagsArray,
                champSquarePortraitURL
            )
            AllChampionData.add(championdata)
            addChampToDatabase(championdata)

            getChampionIndividualJSON(id)
        }

        inputActivity.updateVisuals()


    }








}