package com.robbie.wezacareships

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    var  shipList: ArrayList<Model> =ArrayList()
    var progressDialog: ProgressDialog? = null

    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var recyclerview: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview = findViewById(R.id.recyclerview) as RecyclerView
        recyclerAdapter=  RecyclerAdapter( this, shipList)
        //start progress dialog
        progressDialog = ProgressDialog(this)
        // Setting up message in Progress dialog.
        progressDialog!!.setMessage("Getting ready.")
        ContextCompat.getColorStateList(this, R.color.primary)
        // Showing progress dialog.
        progressDialog!!.show()
        setUpSearchView()
        displayShips()
        if (!isUserOnline(this)) {
            toast("No Network.Check your Internet connection")
        }
        //we need to get an api online


    }

    fun displayShips(){
        val client = AsyncHttpClient(true, 80, 443)

        client.get( "https://api.spacexdata.com/v3/ships",
            object : JsonHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONArray?,
                ) {
                    if (response != null) {
                        for (i in 0 until response.length()) {
                            val json = response.getJSONObject(i)
                            val shipid = json.optString("ship_id").toString()
                            val shipname = json.optString("ship_name").toString()
                            val port = json.optString("home_port").toString()
                            val shiptype = json.optString("ship_type").toString()
                            val shipweight = json.optString("weight_kg").toString()
                            val shipyear = json.optString("year_built").toString()
                            val image = json.optString("image").toString()

                            shipList.add(Model(shipid, shipname, port, image,shiptype,shipweight,shipyear))
                        }
                    }//end for loop

                    //super.onSuccess(statusCode, headers, response)
                    //a add the shiplist to recycle View
                    recyclerAdapter = RecyclerAdapter(this@MainActivity,  shipList)
                    recyclerview.layoutManager = LinearLayoutManager(this@MainActivity)
                    recyclerview.adapter = recyclerAdapter
                    recyclerview.setHasFixedSize(true)
                    progressDialog!!.dismiss()
                }// end success

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseString: String?,
                    throwable: Throwable?,
                ) {
                    Toast.makeText(this@MainActivity, "error", Toast.LENGTH_LONG).show()
                }
            })
    }
    fun setUpSearchView() {
        val searchView = findViewById(R.id.searchView) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.length == 0){
                    displayShips()
                    //Reload the data Again , use the existing  method you have of loading data in the RecyclerView .
                }
                recyclerAdapter.getFilter().filter(newText)
                return true
            }
        })

        searchView.isIconified = true
        searchView.setQuery("", false)
    }

    @Suppress("DEPRECATION")
    fun isUserOnline(context: Context): Boolean {
        try {
            val nConManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nNetworkinfo = nConManager.activeNetworkInfo
            if (nNetworkinfo != null) {
                return nNetworkinfo.isConnected
            }
        } catch (e: Exception) {
        }
        return false
    }

    fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}