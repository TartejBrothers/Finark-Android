package com.example.finarkcontests

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

import android.widget.Button
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signUpButton: Button = findViewById(R.id.button2)
        signUpButton.setOnClickListener {
            val intent = Intent(this, signuppage::class.java)
            startActivity(intent)
        }
    }
}