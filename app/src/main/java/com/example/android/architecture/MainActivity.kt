package com.example.android.architecture

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.architecture.viewModel.DiceViewModel
import kotlinx.android.synthetic.main.activity_main.*

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
    private val viewModel by lazy { ViewModelProvider(this).get(DiceViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // subscribe to view model data for headline and dice
        viewModel.headline.observe(this, Observer { headline.text = it }) // 'it' is the value publish by the view model
        viewModel.dice.observe(this, Observer { updateDisplay(it) })

        // AppCompatActivity implements the LifecycleOwner so we can have access to lifecycle object
        lifecycle.addObserver(MyLifecycleObserver())

        fab.setOnClickListener {
            viewModel.rollDice()
        }
    }

    private fun fabClickHandler() {
        viewModel.rollDice()
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

}
