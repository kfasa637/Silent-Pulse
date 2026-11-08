package com.silentpulse

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(12, 14, 18)
        window.navigationBarColor = Color.rgb(12, 14, 18)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(32, 32, 32, 32)
            setBackgroundColor(Color.rgb(12, 14, 18))
        }

        val title = TextView(this).apply {
            text = "SILENT PULSE"
            textSize = 30f
            setTextColor(Color.WHITE)
            typeface = Typeface.create("sans-serif-black", Typeface.BOLD)
            letterSpacing = 0.12f
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "BEDROCK LAUNCHER"
            textSize = 12f
            setTextColor(Color.rgb(130, 150, 158))
            letterSpacing = 0.22f
            gravity = Gravity.CENTER
        }

        val launchButton = TextView(this).apply {
            text = "ЗАПУСТИТЬ ИГРУ"
            textSize = 15f
            setTextColor(Color.rgb(7, 18, 19))
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            gravity = Gravity.CENTER
            letterSpacing = 0.06f
            background = GradientDrawable().apply {
                setColor(Color.rgb(0, 255, 204))
                cornerRadius = 24f
            }
            setPadding(28, 20, 28, 20)
            setOnClickListener { launchMinecraft() }
        }

        root.addView(title, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ))
        root.addView(subtitle, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = 10 })
        root.addView(launchButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = 56 })

        setContentView(root)
    }

    private fun launchMinecraft() {
        val launchIntent = packageManager.getLaunchIntentForPackage("com.mojang.minecraftpe")
        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(launchIntent)
            finish()
        } else {
            Toast.makeText(this, "Minecraft Bedrock не установлен", Toast.LENGTH_LONG).show()
        }
    }
}
