package com.example.gamebacklog.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gamebacklog.database.CardRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val cardRepository = CardRepo(application.applicationContext)
    private val mainScope = CoroutineScope(Dispatchers.Main)

    var cards = cardRepository.getAllCards()

    fun deleteAllCards() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                cardRepository.deleteAllCards()

            }
        }
    }

    fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cardToDelete = cards.value?.get(position)
                cards.value?.drop(position)
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        cardRepository.deleteCard(cardToDelete!!)
                    }

                }
            }
        }
        return ItemTouchHelper(callback)
    }
}
