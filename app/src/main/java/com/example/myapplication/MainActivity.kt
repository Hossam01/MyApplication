package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.MainViewModel.ViewModelMain
import com.example.myapplication.adapter.ArticlesAdapter
import com.example.myapplication.check.NetworkChangeReceiver
import com.example.myapplication.models.Countitem
import com.example.myapplication.others.DataManager
import com.example.myapplication.others.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@AndroidEntryPoint

class MainActivity : AppCompatActivity() {
    private val mainModel: ViewModelMain by viewModels()
    private lateinit var adapter: ArticlesAdapter
    private lateinit var mNetworkReceiver: BroadcastReceiver


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ArticlesAdapter()
        rvArticleslist.adapter = adapter

        mNetworkReceiver = NetworkChangeReceiver()
        registerNetworkBroadcastForNougat()

        if (isOnline(this) == false)
            mainModel.getLocalData()

        else
            mainModel.getData()

        lifecycleScope.launchWhenStarted {
            mainModel.getUsers().collect {
                when (it.status) {
                    Status.OK -> {
                        load.visibility = View.GONE
                        it.results.let {
                            val splited: List<String> = it!!.split(" ")
                            var item = ArrayList<Countitem>()
                            Log.e("list", splited.toString())
                            for (s in splited) {
                                if (!item.contains(Countitem(s, Collections.frequency(splited, s)))) {
                                    item.add(Countitem(s, Collections.frequency(splited, s)))
                                  //  mainModel.insert(Countitem(s, Collections.frequency(splited, s)))
                                }
                            }
                            adapter.submitList(item)
                        }
                    }
                    Status.LOADING -> {
                        load.visibility = View.VISIBLE
                    }
                    Status.ERROR -> {
                        load.visibility = View.GONE
                        Toast.makeText(
                                this@MainActivity,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        }
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }


    private fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            netInfo != null && netInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }
    }
}