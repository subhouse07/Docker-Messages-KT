package com.example.dockermessageskt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MessageListViewModel: ViewModel() {
    private val messageListLiveData: MutableLiveData<List<Message>> by lazy { MutableLiveData<List<Message>>() }
    init {
        val messageList: List<Message> = listOf()
        messageListLiveData.postValue(messageList)
    }

    fun getMessageList(): LiveData<List<Message>> {
        return messageListLiveData
    }
    fun updateMessageList(updateMessages: List<Message>) {
        messageListLiveData.postValue(updateMessages)
    }

}