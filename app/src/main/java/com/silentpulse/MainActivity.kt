package com.silentpulse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private val accent = Color.rgb(0, 255, 204)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 56, 28, 24)
            setBackgroundColor(Color.rgb(12, 15, 18))
        }
        root.addView(TextView(this).apply {
            text = "SILENT PULSE"
            textSize = 30f
            setTextColor(accent)
            letterSpacing = .12f
        })
        root.addView(TextView(this).apply {
            text = "BEDROCK COMPANION"
            textSize = 12f
            setTextColor(Color.LTGRAY)
            setPadding(0, 10, 0, 42)
        })
        root.addView(button("OPEN FLOATING PANEL") { startPanel() })
        root.addView(button("STOP PANEL") {
            stopService(Intent(this, OverlayService::class.java))
            Toast.makeText(this, "Panel stopped", Toast.LENGTH_SHORT).show()
        })
        root.addView(button("LAUNCH MINECRAFT") { launchMinecraft() })
        root.addView(TextView(this).apply {
            text = "A standalone companion interface. Settings are local previews and do not change Minecraft or interact with its memory."
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(Color.GRAY)
            setPadding(8, 30, 8, 0)
        })
        setContentView(root)
    }
    private fun button(label: String, action: () -> Unit) = Button(this).apply {
        text = label
        isAllCaps = false
        setTextColor(Color.BLACK)
        setBackgroundColor(accent)
        textSize = 14f
        setOnClickListener { action() }
        layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 58).apply { bottomMargin = 12 }
    }
    private fun startPanel() {
        if (!Settings.canDrawOverlays(this)) {
            startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")))
            Toast.makeText(this, "Allow display over other apps, then press OPEN FLOATING PANEL again.", Toast.LENGTH_LONG).show()
            return
        }
        startForegroundService(Intent(this, OverlayService::class.java))
    }
    private fun launchMinecraft() {
        val intent = packageManager.getLaunchIntentForPackage("com.mojang.minecraftpe")
        if (intent == null) Toast.makeText(this, "Minecraft not found.", Toast.LENGTH_LONG).show()
        else startActivity(intent)
    }
}
