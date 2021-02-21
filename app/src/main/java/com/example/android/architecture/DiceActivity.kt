package com.example.android.architecture

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.architecture.viewModel.DiceViewModel

/**
 * Changes made wrt to chap2_end checkin:
 * 1. Rename the MainActivity to DiceActivity
 * 2. Create a new MainActivity as the launcher activity and parent of DiceActivity
 * 3. Add a button in the MainActivity and configure to navigate to DiceActivity
 * 4. Remove the fab button from the DiceActivity
 * 5. Handle dice rolling when there is a config change
 * 6. Use implicit intent to share data between activity
 */
class DiceActivity : AppCompatActivity() {

    // the following code is Kotlin's lazy instantiation of variable, which means
    // it is effectively declared now, but only instantiated and assigned when it is first needed.
    // Also note that for imageViews and headline widget, we are using findViewById to create them instead of synthetic view.
    private val imageViews by lazy {
        arrayOf<ImageView>(
            findViewById(R.id.die1),  // determine our widget instead of using synthetic view bindings
            findViewById(R.id.die2),
            findViewById(R.id.die3),
            findViewById(R.id.die4),
            findViewById(R.id.die5)
        )
    }
    private val headline by lazy { findViewById<TextView>(R.id.headline) }
    private val toolbar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val viewModel by lazy { ViewModelProvider(this).get(DiceViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dice)
        setSupportActionBar(toolbar)

        // This will provide a up arrow on the action bar to allow user to go back to previous activity
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back) // optional: specify icon to use for up arrow action

        // subscribe to view model data for headline and dice
        viewModel.headline.observe(this, Observer { headline.text = it }) // 'it' is the value publish by the view model
        viewModel.dice.observe(this, Observer { updateDisplay(it) })

        // AppCompatActivity implements the LifecycleOwner so we can have access to lifecycle object
        lifecycle.addObserver(MyLifecycleObserver())

        val configChange = savedInstanceState?.getBoolean(CONFIG_CHANGE) ?: false
        if(!configChange) viewModel.rollDice() // roll the dice only there is no config change
    }

    private fun updateDisplay(dice:IntArray) {
        for (i in 0 until imageViews.size) {
            val drawableId = when (dice[i]) {
                1 -> R.drawable.die_1
                2 -> R.drawable.die_2
                3 -> R.drawable.die_3
                4 -> R.drawable.die_4
                5 -> R.drawable.die_5
                6 -> R.drawable.die_6
                else -> R.drawable.die_6
            }
            imageViews[i].setImageResource(drawableId)
        }
    }

    // The code is to handle the situation when there is a configuration change - device rotation.
    // onSaveInstanceState event is triggered everytime the device rotate its orientation.
    // We save the status of a config change so that the app will not roll the dice when there is a config change.
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(CONFIG_CHANGE, true)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dice, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_share -> shareResult()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareResult(): Boolean {
        /*
        An Intent is a messaging object you can use to request an action from another app component.
        Here we use the .apply method which in Kotlin allow us to set the object properties
        without having to specify the object variable name. The following apply block is equivalent to :
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Irolled the dice: ${viewModel.headline.value}")
            intent.type = "text/plain"
         */
        val intent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "I rolled the dice: ${viewModel.headline.value}")
                                    type = "text/plain"
                                }
        startActivity(intent)
        return true
    }
}
