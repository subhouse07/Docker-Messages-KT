package com.example.dockermessageskt

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MessageClient constructor(context: Context){
    private val requestQueue = Volley.newRequestQueue(context)
    private val sendTag = "SEND_TAG"
    private val getTag = "GET_TAG"

    init {
        requestQueue.start()
    }

    fun cancelRequests() {
        requestQueue.cancelAll(getTag)
        requestQueue.cancelAll(sendTag)
        requestQueue.stop()
    }

    fun getMessagesJSON(
        responseListener: Response.Listener<JSONArray>,
        errorListener: Response.ErrorListener) {

        val request = JsonArrayRequest(
            Request.Method.GET,
            "${ServerConstants.BASE_URL}${ServerConstants.ENDPOINT_GET}",
            null,
            responseListener,
            errorListener
        ).setTag(getTag)
        requestQueue.add(request)
    }

    fun sendMessage(
        message: Message,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
        ) {

        val messageJSON = JSONObject()
        messageJSON
            .put("sender", message.sender)
            .put("msgText", message.msgText)
        val request = object: JsonObjectRequest(
            Method.POST,
            "${ServerConstants.BASE_URL}${ServerConstants.ENDPOINT_SEND}",
            messageJSON,
            responseListener,
            errorListener) {

            override fun getHeaders(): MutableMap<String, String> {
                return mutableMapOf("Content-Type" to "application/json")
            }
        }
        request.tag = sendTag
        requestQueue.add(request)
    }
}