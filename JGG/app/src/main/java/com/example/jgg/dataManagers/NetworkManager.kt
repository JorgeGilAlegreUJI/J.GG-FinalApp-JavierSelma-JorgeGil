package com.example.jgg.dataManagers

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.covidstats.RecyclerAdapter
import com.example.jgg.InputActivity
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
        val url = "https://ddragon.leagueoflegends.com/cdn/11.12.1/data/en_US/champion.json"
        query(url, ::insertAllChampionsInfo, ::manageNetworkError, inputActivity)
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

                // Your code here
            }




            val championdata = ChampionData(
                name,
                title,
                attackValue,
                defenseValue,
                magicValue,
                difficultyValue,
                champTagsArray,
                champSquarePortraitURL
            )
            AllChampionData.add(championdata)
        }

        inputActivity.updateVisuals()


    }








}