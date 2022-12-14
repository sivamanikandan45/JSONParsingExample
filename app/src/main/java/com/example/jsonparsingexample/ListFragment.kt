package com.example.jsonparsingexample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONTokener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch {
            val manager=LinearLayoutManager(context)
            val adapter:ListAdapter
            var holidayList: MutableList<Holiday> = mutableListOf()
            val job=GlobalScope.launch {
                 holidayList= doNetworkOperation()

            }
            //delay(10000L)
            job.join()
            println("Got!!!")
            adapter=ListAdapter(holidayList)
            //val jsonObject = JSONTokener(con).nextValue() as JSONObject
            GlobalScope.launch(Dispatchers.Main) {
                val recyclerView=view.findViewById<RecyclerView>(R.id.recycler)
                recyclerView.adapter=adapter
                recyclerView.layoutManager=manager
            }

        }

    }

    private suspend fun doNetworkOperation(): MutableList<Holiday> {
        var holidayList: MutableList<Holiday> = mutableListOf()
        withContext(Dispatchers.IO){
            val url="https://date.nager.at/api/v3/publicholidays/2022/us"
            //val url = "https://raw.githubusercontent.com/Hipo/university-domains-list/master/world_universities_and_domains.json"
            //val url="https://datausa.io/api/data?drilldowns=Nation&measures=Population"
            val connection = URL(url).openConnection() as HttpURLConnection
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            //val reader=BufferedReader(bufferreader)
            var line = reader.readLine()
            var string = ""
            while (line != null) {
                string += line
                line = reader.readLine()
            }

            if (string.isNotEmpty()) {
                //val jsonObject=JSONObject(string)
                //val jsonArray=jsonObject.getJSONArray("data")
                val jsonArray = JSONTokener(string).nextValue() as JSONArray
                for (i in 0 until jsonArray.length()) {
                    val holiday = jsonArray.getJSONObject(i)
                    val day = holiday.getString("localName")
                    //val jsonArray=jsonObject.getJSONArray("date")
                    val date = holiday.getString("date")
                    holidayList.add(Holiday(day, date))
                }
            }
        }
        return holidayList
    }
}