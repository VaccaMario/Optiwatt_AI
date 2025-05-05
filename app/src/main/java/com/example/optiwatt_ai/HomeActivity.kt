package com.example.optiwatt_ai

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val ringChart = findViewById<RingChartView>(R.id.ringChart)
        ringChart.percent = 0.75f
        ringChart.invalidate()

        val menuButton = findViewById<ImageView>(R.id.menuButton)

        menuButton.setOnClickListener {
            val popup = PopupMenu(this, menuButton)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.go_to_provider -> {
                        startActivity(Intent(this, ProviderActivity::class.java))
                        true
                    }
                    R.id.go_to_home -> {
                        startActivity(Intent(this, HomeActivity::class.java))
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }
}
