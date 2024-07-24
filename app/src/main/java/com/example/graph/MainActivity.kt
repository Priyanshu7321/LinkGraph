package com.example.graph

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.privacysandbox.tools.core.model.Method
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.ResponseDelivery
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.androidgamesdk.gametextinput.Listener
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.Locale
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {
    private var url="https://api.inopenapp.com/"

    private lateinit var requestQueue: RequestQueue
    private lateinit var lineChart: LineChart
    private lateinit var recyclerView: RecyclerView
    private lateinit var appInfoAdapter: AppInfoAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var recentLinks1 = ArrayList<layout_item>()
    private var topLinks1 = ArrayList<layout_item>()
    private val layoutItems = ArrayList<layout_item>()
    private lateinit var viewModel: LinksViewModel
    companion object {
        private const val TAG = "MainActivity"
        private const val BASE_URL = "https://api.inopenapp.com/api/v1/dashboardNew"
        private const val TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjU5MjcsImlhdCI6MTY3NDU1MDQ1MH0.dCkW0ox8tbjJA2GgUx2UEwNlbTZ7Rr38PVFJevYcXFI"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        tabLayout= findViewById(R.id.tabLink)
        viewPager= findViewById(R.id.viewPager)
        viewModel=ViewModelProvider(this).get(LinksViewModel::class.java)
        requestQueue = Volley.newRequestQueue(this)
        lineChart = findViewById(R.id.lineGraph)

        fetchDataWithToken()
        setupViewPager()
    }

    private fun fetchDataWithToken() {
        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET, BASE_URL, null,
            com.android.volley.Response.Listener { response ->
                try {
                    val data = response.getJSONObject("data")

                    val recentLinks = data.getJSONArray("recent_links")
                    val topLinks = data.getJSONArray("top_links")

                    for (i in 0 until recentLinks.length()) {
                        val link = recentLinks.getJSONObject(i)
                        val uri1 = link.getString("original_image")
                        val appName1 = link.getString("app")
                        val date1 = link.getString("created_at")
                        val totalClick1 = link.getInt("total_clicks")
                        val link1 = link.getString("web_link")

                        val layoutItem = layout_item(uri1, appName1, date1,
                            totalClick1, link1)
                        layoutItems.add(layoutItem)
                        recentLinks1.add(layoutItem)

                    }

                    for (i in 0 until topLinks.length()) {
                        val link = topLinks.getJSONObject(i)
                        val uri1 = link.getString("original_image")
                        val appName1 = link.getString("app")
                        val date1 = link.getString("created_at")
                        val totalClick1 = link.getInt("total_clicks")
                        val link1 = link.getString("web_link")

                        val layoutItem = layout_item(uri1, appName1, date1, totalClick1, link1)
                        layoutItems.add(layoutItem)
                        topLinks1.add(layoutItem)
                    }
                    viewModel.setRecentLinks(recentLinks1)
                    viewModel.setTopLinks(topLinks1)
                    displayChart()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            com.android.volley.Response.ErrorListener { error ->
                Log.e(TAG, "Error: ${error.message}")
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $TOKEN"
                return headers
            }
        }

        requestQueue.add(jsonObjectRequest)
    }

    private fun displayChart() {
        val entries = layoutItems.mapNotNull {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = sdf.parse(it.date1) ?: return@mapNotNull null
            val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
            val month = monthFormat.format(date)
            val monthIndex = getMonthIndex(month)

            Entry(monthIndex.toFloat(), it.totalClicks1.toFloat())
        }

        val dataSet = LineDataSet(entries, "Total Clicks")
        dataSet.color = Color.BLUE
        dataSet.valueTextColor = Color.BLACK
        dataSet.lineWidth = 2f
        dataSet.setDrawFilled(true)
        dataSet.fillColor = Color.BLUE
        dataSet.fillAlpha = 100
        dataSet.setDrawCircles(false)

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)

        val leftAxis = lineChart.axisLeft
        leftAxis.setDrawGridLines(false)
        val rightAxis = lineChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.isEnabled = false

        val description = Description()
        description.text = "Clicks per Month"
        lineChart.description = description

        lineChart.invalidate()
    }

    private fun getMonthIndex(month: String): Int {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        return months.indexOf(month) + 1
    }
    private fun setupViewPager() {
        val pagerAdapter = FragmentPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Recent Links" else "Top Links"
        }.attach()
    }

}