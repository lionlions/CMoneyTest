package com.cindy.cmoneytest.api

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ApiRepository(val context: Context?) {

    private val TAG: String = javaClass.simpleName

    fun getListData(apiCallBack: ApiCallBack){
        val queue: RequestQueue = Volley.newRequestQueue(context)
        val url: String = "https://raw.githubusercontent.com/cmmobile/NasaDataSet/main/apod.json"
        val stringRequest: StringRequest = StringRequest(Request.Method.GET, url, { response ->
            apiCallBack.onGetListDataDone(response)
        }, { error ->
            Log.w(TAG, "error: ${error.message}")
        })

        queue.add(stringRequest)
    }

}