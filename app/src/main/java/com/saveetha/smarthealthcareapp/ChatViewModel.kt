package com.saveetha.smarthealthcareapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _messageList = MutableLiveData<MutableList<MessageModel>>(mutableListOf())
    val messageList: LiveData<MutableList<MessageModel>> = _messageList

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = Constants.apiKey
    )

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = _messageList.value!!.map {
                        content(it.role) { text(it.message) }
                    }
                )

                _messageList.value!!.add(MessageModel(question, "user"))
                _messageList.value!!.add(MessageModel("Typing...", "model"))
                _messageList.postValue(_messageList.value)

                val response = chat.sendMessage(question)
                _messageList.value!!.removeAt(_messageList.value!!.lastIndex)

                _messageList.value!!.add(
                    MessageModel(response.text ?: "No response", "model")
                )
                _messageList.postValue(_messageList.value)

            } catch (e: Exception) {
                _messageList.value!!.removeAt(_messageList.value!!.lastIndex)

                _messageList.value!!.add(MessageModel("Error: ${e.message}", "model"))
                _messageList.postValue(_messageList.value)
            }
        }
    }
}
