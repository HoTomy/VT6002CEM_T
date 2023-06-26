package com.example.vt6002c

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnC = findViewById<Button>(R.id.hc_b)
        val btnA = findViewById<Button>(R.id.ae_b)
        val btnS = findViewById<Button>(R.id.sh_b)

        btnA.setOnClickListener {
            val intent = Intent(this, AE::class.java)
            startActivity(intent)
        }

        btnC.setOnClickListener {
            val intent = Intent(this, Clinic::class.java)
            startActivity(intent)
        }

        btnS.setOnClickListener {
            val intent = Intent(this, BiLi::class.java)
            startActivity(intent)
        }

    }
}