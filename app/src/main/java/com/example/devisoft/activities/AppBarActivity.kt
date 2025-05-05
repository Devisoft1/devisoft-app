package com.example.devisoft.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.devisoft.R

class AppBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_bar)

        val logo = findViewById<ImageView>(R.id.appBarLogo)

        logo.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)

        // Add first item: "Website"
        popupMenu.menu.add(0, 1, 0, "Website")  // (groupId, itemId, order, title)

        // You can add more items if needed:
        // popupMenu.menu.add(0, 2, 1, "About Us")

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                1 -> { // Website
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://devisoft.co.in/"))
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}
