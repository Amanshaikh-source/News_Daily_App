package com.example.newsdaily

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsdaily.ui.theme.APIRequest
import com.example.newsdaily.ui.theme.RecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//const val BASE_URL = "https://api.currentsapi.services"
const val BASE_URL = "https://newsapi.org"

class MainActivity : AppCompatActivity() {

    lateinit var countdownTimer: CountDownTimer
    private var seconds = 2L

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()
    private var publishedList = mutableListOf<String>()
    private var authorList = mutableListOf<String>()
    private var contentList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Daily News"
        toolbar.setSubtitle("Powered By Aman Shaikh")
        makeAPIRequest()
    }

    //simple fade in animation for when the app is done loading
    private fun fadeIn() {
        val v_blackScreen = findViewById<View>(R.id.v_blackScreen)
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 2000
        }.start()
    }

    //requests data from the api and forwards it to the recycler view
    private fun makeAPIRequest() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.articles) {
                    Log.d("MainActivity", "Result + $article")
                    addToList(
                        article.title,
                        article.description,
                        article.urlToImage,
                        article.url,
                        article.publishedAt,
                        article.author,
                        article.content
                    )
                }

                //updates ui when data has been retrieved
                withContext(Dispatchers.Main) {
                    setUpRecyclerView()
                    fadeIn()
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.d("MainActivity", e.toString())
                withContext(Dispatchers.Main) {
                    attemptRequestAgain(seconds)

                }
            }

        }
    }

    private fun attemptRequestAgain(seconds: Long) {
        val tv_noInternetCountDown = findViewById<TextView>(R.id.tv_noInternetCountDown)
        countdownTimer = object : CountDownTimer(seconds * 1010, 1000) {
            override fun onFinish() {
                makeAPIRequest()
                countdownTimer.cancel()
                tv_noInternetCountDown.visibility = View.GONE
                this@MainActivity.seconds += 0
            }

            override fun onTick(millisUntilFinished: Long) {
                tv_noInternetCountDown.visibility = View.VISIBLE
                tv_noInternetCountDown.text =
                    "Cannot retrieve data...\nTrying again in: ${millisUntilFinished / 1000}"
                Log.d(
                    "MainActivity",
                    "Could not retrieve data. Trying again in ${millisUntilFinished / 1000} seconds"
                )
            }
        }
        countdownTimer.start()
    }

    private fun setUpRecyclerView() {
        val rv_recyclerView = findViewById<RecyclerView>(R.id.rv_recyclerView)
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter = RecyclerAdapter(
            titlesList,
            descList,
            imagesList,
            linksList,
            publishedList,
            authorList,
            contentList
        )
    }

    //adds the items to our recyclerview
    private fun addToList(
        title: String,
        description: String,
        image: String,
        link: String,
        publishedAT: String,
        author: String,
        content: String
    ) {
        linksList.add(link)
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        publishedList.add(publishedAT)
        authorList.add(author)
        contentList.add(content)
    }
}