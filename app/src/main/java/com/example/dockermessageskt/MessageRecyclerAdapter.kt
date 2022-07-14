package com.example.dockermessageskt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageRecyclerAdapter(private var dataSet: List<Message>, private val context: Context): RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {

    fun updateItems(messages: List<Message>) {
        val prevSize = dataSet.size
        val newMsgCount =  messages.size - dataSet.size
        dataSet = messages
        notifyItemRangeChanged(prevSize, newMsgCount)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageTextView: TextView = view.findViewById(R.id.itemText)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.message_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val senderText = dataSet[position].sender
        val messageText = dataSet[position].msgText
        viewHolder.messageTextView.text = context.resources.getString(R.string.string_message, senderText, messageText)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

}