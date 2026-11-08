package com.silentpulse

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val background = FrameLayout(this).apply {
            setBackgroundColor(Color.parseColor("#121212"))
        }

        val launchButton = Button(this).apply {
            text = "ЗАПУСТИТЬ ИГРУ"
            textSize = 20f
            setTextColor(Color.parseColor("#121212"))
            setBackgroundColor(Color.parseColor("#00FFCC"))
            isAllCaps = false
            setOnClickListener {
                val intent: Intent? =
                    packageManager.getLaunchIntentForPackage("com.mojang.minecraftpe")

                if (intent != null) {
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Minecraft не установлен!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val buttonParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
            leftMargin = dp(28)
            rightMargin = dp(28)
        }

        background.addView(launchButton, buttonParams)
        setContentView(background)
    }

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }
}
