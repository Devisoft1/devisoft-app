package com.example.devisoft.activities

import android.content.Intent
import android.net.Uri
import com.example.devisoft.R
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AppBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar) // Replace with your actual layout name

        val logo = findViewById<ImageView>(R.id.appBarLogo)
        logo.setOnClickListener {
            val url = "https://devisoft.co.in/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}
