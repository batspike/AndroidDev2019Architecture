package com.example.android.architecture

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.android.architecture.util.DiceHelper
import com.example.android.architecture.viewModel.DiceViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

/**
 * The above kotlinx.android.synthetic.main.<layout name>.* imported package
 * provides all the synthetic view bindings with the corresponding layout files. The package
 * library will generates all the objects and identifier from the layout files using its
 * specified @+id.
 * For example, we have toolbar and fab generated from activity_main.xml layout file.
 * We can also have id headline, and die1..die6 generated from content_main.xml layout file, but
 * chose to declare and find it ourselves using findViewById() function.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel:DiceViewModel

    // the following 2 properties has been moved to DiceViewModel class
//    private lateinit var dice: IntArray
//    private lateinit var headlineText: String

    // the following code is Kotlin's lazy instantiation of variable, which means
    // it is effectively declared now, but only instantiated and assigned when it is first needed.
    private val imageViews by lazy {
        arrayOf<ImageView>(
            findViewById(R.id.die1),  // determine our widget instead of using synthetic view bindings
            findViewById(R.id.die2),
            findViewById(R.id.die3),
            findViewById(R.id.die4),
            findViewById(R.id.die5)
        )
    }

    // lateinit allow us to declare var without initializing it; alternatively we can declare like above imageViews usiing lazy
    private lateinit var headline:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // instantiate the view model and subscribe to its data, headline and dice
        viewModel = ViewModelProvider(this).get(DiceViewModel::class.java)
        viewModel.headline.observe(this, Observer { headline.text = it }) // 'it' is a value publish by the view model
        viewModel.dice.observe(this, Observer { updateDisplay(it) })

        // restoring the app state from Bundle object
//        headlineText = savedInstanceState?.getString(HEADLINE_TEXT) ?: getString(R.string.welcome)
//        dice = savedInstanceState?.getIntArray(DICE_COLLECTION) ?: intArrayOf(6,6,6,6,6)
        // the above 2 lines is no longer needed with view model

        // AppCompatActivity implements the LifecycleOwner so we can have access to lifecycle object
        lifecycle.addObserver(MyLifecycleObserver())

        // note the parathesis is optional for parameter that is a lambda expression.
        fab.setOnClickListener {
//            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
            // replacing the above Snackbar lambda with Toast widget handler which does not require a view
//            fabClickHandler()
            viewModel.rollDice() // this replace fabClickHandler() when using view model
        }

        headline = findViewById(R.id.headline) // using findViewById instead of using synthetic view bindings

        // the following is no longer needed since we restore the state with defaults from Bundle object above - line48.
//        headline.text = getString(R.string.welcome)
//        for(imageView in imageViews) { //initialize all the dice to 6; this is where imageViews get instantiated.
//            imageView.setImageResource(R.drawable.die_6)
//        }

//        updateDisplay() // not needed with view model

    }

    private fun fabClickHandler() {
//        Toast.makeText(this, "You are toast!", Toast.LENGTH_LONG).show()

//        dice = DiceHelper.rollDice()
//        headlineText = DiceHelper.evaluateDice(this, dice)
//        updateDisplay()
        viewModel.rollDice() // this replace the above 3 lines using view model
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
//        headline.text = headlineText  // no longer needed with view model
    }

    // The Bundle object is used to save and restore the state of the app for
    // ensuring its state is not reinitialize when the device is rotated.
//    override fun onSaveInstanceState(outState: Bundle) {
//        // saving the state for restoration in the onCreate event
//        outState.putString(HEADLINE_TEXT, headlineText)
//        outState.putIntArray(DICE_COLLECTION, dice)
//        super.onSaveInstanceState(outState)
//    }
    // the above function is no longer needed with view model

}
