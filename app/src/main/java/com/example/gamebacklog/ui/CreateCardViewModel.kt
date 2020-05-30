package com.example.gamebacklog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.gamebacklog.database.CardRepo
import com.example.gamebacklog.model.Card
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCardViewModel(application: Application) : AndroidViewModel(application) {

    private val cardRepository = CardRepo(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val card = MutableLiveData<Card?>()
    val error = MutableLiveData<String?>()
    val success = MutableLiveData<Boolean?>()

    fun updateCard() {
        if (isCardValid()) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    cardRepository.updateCard(card.value!!)
                }
                success.value = true
            }
        }
    }

    fun insertCard() {
        if (isCardValid()) {
            mainScope.launch {
                withContext(Dispatchers.IO) {
                    cardRepository.insertCard(card.value!!)
                }
                success.value = true
            }
        }
    }

    fun deleteAllCards() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                cardRepository.deleteAllCards()

            }
        }
    }

    private fun isCardValid(): Boolean {
        return when {
            card.value!!.platform.isBlank() -> {
                error.value = "Platform must not be empty"
                false
            }
            card.value!!.title.isBlank() -> {
                error.value = "Title must not be empty"
                false
            }
            else -> true
        }
    }
}