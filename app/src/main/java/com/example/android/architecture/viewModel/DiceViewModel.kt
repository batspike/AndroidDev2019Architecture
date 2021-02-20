package com.example.android.architecture.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.android.architecture.LOG_TAG
import com.example.android.architecture.R
import com.example.android.architecture.util.DiceHelper

/**
 * A ViewModel (one of the lifecycle objects) is designed to manage data at runtime.
 * Each activity or fragment can have its own view model. The view model store data persistently
 * in memory and it sticks around even during conficuration changes (device rotation).
 * So even when the activity is stopped or destroyed, the view model stays in memory.
 * The view model can publish its data, and activity or fragment can subscribe to it.
 */
class DiceViewModel(app:Application) : AndroidViewModel(app) {

    // view model properties
    val headline = MutableLiveData<String>()
    val dice = MutableLiveData<IntArray>()
    private val context = app //private bcos only needed within this class

    init {
        Log.i(LOG_TAG, "View model created")

        // initialize properties values
        headline.value = context.getString(R.string.welcome)
        dice.value = intArrayOf(6,6,6,6,6)
    }

    fun rollDice() {
        dice.value = DiceHelper.rollDice()
        headline.value = DiceHelper.evaluateDice(context, dice.value) // use .value to access view model data value
    }
}