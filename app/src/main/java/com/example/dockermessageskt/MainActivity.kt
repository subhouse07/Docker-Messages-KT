package com.example.dockermessageskt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONException
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    private val messageListViewModel : MessageListViewModel by viewModels()
    private lateinit var messageClient : MessageClient
    private lateinit var messageRecyclerAdapter : MessageRecyclerAdapter
    private lateinit var messageRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageClient = MessageClient(this)
        initRecyclerView()
        initViewModel()
        initButtonListeners()

    }

    override fun onStart() {
        super.onStart()
        fetchMessages()
    }

    override fun onStop() {
        super.onStop()
        messageClient.cancelRequests()
    }

    private fun fetchMessages() {
        messageClient.getMessagesJSON(responseListener = {
            val messages: MutableList<Message> = mutableListOf()
            for (i in 0..it.length()) {
                try {
                    val messageJSON = it.getJSONObject(i)
                    messages.add(Message(
                        "0",
                        messageJSON.getString("sender"),
                        messageJSON.getString("msgText")
                    ))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            messageListViewModel.updateMessageList(messages)
        }, errorListener = {
            Log.e(MainActivity::class.java.name, it.message.toString())
        })
    }

    private fun initButtonListeners() {
        val senderEditText = findViewById<EditText>(R.id.editTextSender)
        val messageEditText = findViewById<EditText>(R.id.editTextMessage)
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val sender = senderEditText.text.toString()
            val message = messageEditText.text.toString()
            val m = Message("0", sender, message)
            try {
                messageClient.sendMessage(
                    m,
                    responseListener = {
                        Log.d(MainActivity::class.java.name, it.toString())
                    },
                    errorListener = {
                        Log.e(MainActivity::class.java.name, it.toString())
                    }
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            messageEditText.setText("")
        }
        findViewById<Button>(R.id.refreshButton).setOnClickListener { fetchMessages() }
    }

    private fun initRecyclerView() {
        messageRecyclerAdapter = MessageRecyclerAdapter(listOf(), this)
        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageRecyclerAdapter
    }

    private fun initViewModel() {
        messageListViewModel.getMessageList().observe(this) { messages ->
            if (messages.isNotEmpty()) {
                messageRecyclerAdapter.updateItems(messages)
                messageRecyclerView.smoothScrollToPosition(messages.size - 1)
            }
        }
    }
}