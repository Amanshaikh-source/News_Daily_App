    package com.example.newsdaily

    import android.annotation.SuppressLint
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.util.Log
    import android.widget.TextView
    import androidx.appcompat.widget.Toolbar
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
    import com.example.newsdaily.ui.theme.ApiClient
    import com.example.newsdaily.ui.theme.ApiService
    import com.example.newsdaily.ui.theme.New
    import com.example.newsdaily.ui.theme.RecyclerAdapter
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import java.text.SimpleDateFormat
    import java.util.Date


    class MainActivity : AppCompatActivity() {

        private lateinit var adapter: RecyclerAdapter
        private lateinit var recyclerView: RecyclerView
        private lateinit var swipeRefreshLayout: SwipeRefreshLayout
        private lateinit var tvLastUpdated : TextView
        private val newsList = mutableListOf<New>()


        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Daily News"
            toolbar.setSubtitle("Powered By Aman Shaikh")

            recyclerView = findViewById(R.id.rv_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(this)

            adapter = RecyclerAdapter(newsList)
            recyclerView.adapter = adapter

            tvLastUpdated = findViewById(R.id.tvLastUpdated)


            fetchnews()

            swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
            swipeRefreshLayout.setOnRefreshListener {
                fetchnews()
            }

        }
        override fun onResume() {
            super.onResume()
            fetchnews()
        }

        private fun updateTimeStamp(){

            val sdf = SimpleDateFormat("hh:mm a, MMM dd") // Example: 03:45 PM, Feb 25
            val currentTime = sdf.format(Date())
            tvLastUpdated.text = "Last Updated : $currentTime"
            tvLastUpdated.alpha = 1f

            Handler(Looper.getMainLooper()).postDelayed({
                tvLastUpdated.animate()
                    .alpha(0f)
                    .setDuration(1000)
                    .start()
            },5000)
        }
        private fun fetchnews() {
            val apiService = ApiClient.retrofit.create(ApiService::class.java)

                CoroutineScope(Dispatchers.IO).launch { // Run in background thread
                    try {
                        val response = apiService.getTopHeadlines("7ded0c30f3cffbccdd0cfa3f501e5c9a")

                        withContext(Dispatchers.Main) {
                            swipeRefreshLayout.isRefreshing = false
                            if (response.isSuccessful) {
                                val newsResponse = response.body()
                                newsResponse?.let {
                                    newsList.clear()
                                    newsList.addAll(it.articles)
                                    adapter.updateData(newsList)
                                    updateTimeStamp()
                                }
                            } else {
                                Log.e("API_ERROR", "Error Code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main){
                            swipeRefreshLayout.isRefreshing = false
                            Log.e("NETWORK_ERROR", "Exception: ${e.message}")
                        }

                    }
                }

        }

    }
