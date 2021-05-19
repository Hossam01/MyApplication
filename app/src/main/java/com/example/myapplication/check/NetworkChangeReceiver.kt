package com.example.myapplication.check

import android.R
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.others.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.MutableStateFlow


class NetworkChangeReceiver : BroadcastReceiver() {
    companion object {
        var _res = MutableLiveData<Boolean>(false)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            if (isOnline(context!!)) {
                this.resultData="True"
                _res.value=true
                Snackbar.make((context as Activity).findViewById(R.id.content), "Online Connect Intenet", Snackbar.LENGTH_LONG).show()
            } else {
                this.resultData="False"
                _res.value=false
                Snackbar.make((context as Activity).findViewById(R.id.content), "Conectivity Failure !!! ", Snackbar.LENGTH_LONG).show()
            }
        } catch (e: java.lang.NullPointerException) {
            e.printStackTrace()
        }
    }

    fun isOnline(context: Context): Boolean {
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