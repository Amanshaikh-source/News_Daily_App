    package com.example.newsdaily

    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import androidx.appcompat.widget.Toolbar
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.example.newsdaily.ui.theme.ApiClient
    import com.example.newsdaily.ui.theme.ApiService
    import com.example.newsdaily.ui.theme.New
    import com.example.newsdaily.ui.theme.NewsResponse
    import com.example.newsdaily.ui.theme.RecyclerAdapter
    import kotlinx.coroutines.CoroutineScope
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext
    import retrofit2.Call
    import retrofit2.Response


    class MainActivity : AppCompatActivity() {

        private lateinit var adapter: RecyclerAdapter
        private lateinit var recyclerView: RecyclerView
        private val newsList = mutableListOf<New>()


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

            fetchnews()
        }
        override fun onResume() {
            super.onResume()
            fetchnews()
        }

             private fun fetchnews() {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)

                CoroutineScope(Dispatchers.IO).launch { // Run in background thread
                    try {
                        val response = apiService.getTopHeadlines("7ded0c30f3cffbccdd0cfa3f501e5c9a")

                        withContext(Dispatchers.Main) { // Switch to main thread for UI updates
                            if (response.isSuccessful) {
                                val newsResponse = response.body()
                                newsResponse?.let {
                                    newsList.clear()
                                    newsList.addAll(it.articles)
                                    adapter.updateData(newsList)
                                }
                            } else {
                                Log.e("API_ERROR", "Error Code: ${response.code()}, Message: ${response.errorBody()?.string()}")
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("NETWORK_ERROR", "Exception: ${e.message}")
                    }
                }

        }

    }
