package com.example.android.architecture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * The app has two activity, MainActivity and DiceActivity.
 * When the app starts the launcher will lauch the MainActivity as defined in the
 * manifest file. The DiceActivity is a child of the MainAcitivity.
 * To invoke another activity, we use the startActivity with a intent object.
 */
class MainActivity : AppCompatActivity() {
    private val rollButton by lazy { findViewById<Button>(R.id.rollButton) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rollButton.setOnClickListener({
            val intent = Intent(this, DiceActivity::class.java)
            startActivity(intent)
        })
    }
}