package com.silentpulse
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import android.widget.LinearLayout
import android.widget.Button
import android.view.Gravity
import android.graphics.Color

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setBackgroundColor(Color.parseColor("#121212"))
        val playBtn = Button(this)
        playBtn.text = "ЗАПУСТИТЬ ИГРУ"
        playBtn.setBackgroundColor(Color.parseColor("#00FFCC"))
        playBtn.setTextColor(Color.parseColor("#121212"))
        playBtn.setOnClickListener {
            val intent = packageManager.getLaunchIntentForPackage("com.mojang.minecraftpe")
            if (intent != null) {
                Toast.makeText(this, "Запуск Minecraft...", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Minecraft не установлен!", Toast.LENGTH_LONG).show()
            }
        }
        layout.addView(playBtn)
        setContentView(layout)
    }
}
