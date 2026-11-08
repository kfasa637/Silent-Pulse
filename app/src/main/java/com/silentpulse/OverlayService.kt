package com.silentpulse

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.*

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private var panel: LinearLayout? = null
    private var params: WindowManager.LayoutParams? = null
    private val accent = Color.rgb(0, 255, 204)
    private val bg = Color.rgb(15, 19, 22)
    private val options = linkedMapOf(
        "Combat" to listOf("Aim assist preview", "Combo counter", "Hit timing guide", "Reach indicator demo"),
        "Visuals" to listOf("FPS counter (local)", "Coordinates note", "Crosshair preview", "Color theme"),
        "Cosmetics" to listOf("Wings preview", "Cape preview", "Halo preview", "Trail preview"),
        "Misc" to listOf("Compact panel", "Sound feedback preview", "Show status label", "Remember panel position")
    )
    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        createChannel()
        val notification = if (Build.VERSION.SDK_INT >= 26) Notification.Builder(this, CHANNEL)
            else Notification.Builder(this)
        startForeground(NOTIFICATION_ID, notification.setContentTitle("Silent Pulse")
            .setContentText("Floating companion panel is active")
            .setSmallIcon(android.R.drawable.ic_menu_view).setOngoing(true).build())
        showPanel()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY
    private fun showPanel() {
        if (panel != null) return
        val density = resources.displayMetrics.density
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(14, 12, 14, 14)
            setBackgroundColor(bg)
        }
        val header = TextView(this).apply {
            text = "☰  SILENT PULSE     •     BEDROCK"
            textSize = 14f
            setTextColor(accent)
            setPadding(4, 8, 4, 16)
        }
        root.addView(header)
        header.setOnTouchListener { _, event -> drag(event); true }
        val tabs = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        content.setPadding(0, 12, 0, 4)
        fun renderTab(name: String) {
            content.removeAllViews()
            content.addView(TextView(this).apply {
                text = name.uppercase() + "  /  LOCAL PREVIEW"
                textSize = 11f
                setTextColor(Color.GRAY)
                setPadding(4, 4, 4, 12)
            })
            options[name].orEmpty().forEach { label ->
                content.addView(CheckBox(this).apply {
                    text = label
                    textSize = 13f
                    setTextColor(Color.WHITE)
                    buttonTintList = android.content.res.ColorStateList.valueOf(accent)
                    setPadding(2, 3, 2, 3)
                })
            }
            content.addView(TextView(this).apply {
                text = "Preview controls only — no game memory, packets or player state are accessed."
                textSize = 10f
                setTextColor(Color.GRAY)
                setPadding(4, 12, 4, 4)
            })
        }
        options.keys.forEach { name ->
            tabs.addView(Button(this).apply {
                text = name
                textSize = 10f
                isAllCaps = false
                setOnClickListener { renderTab(name) }
            }, LinearLayout.LayoutParams(0, 42, 1f))
        }
        root.addView(tabs)
        root.addView(content)
        val close = Button(this).apply {
            text = "CLOSE PANEL"
            isAllCaps = false
            setOnClickListener { stopSelf() }
        }
        root.addView(close)
        renderTab("Combat")
        val type = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE
        params = WindowManager.LayoutParams((340 * density).toInt(), WindowManager.LayoutParams.WRAP_CONTENT,
            type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT).apply { gravity = Gravity.TOP or Gravity.END; x = 16; y = 100 }
        wm.addView(root, params)
        panel = root
    }
    private fun drag(event: MotionEvent): Boolean {
        val p = params ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> { lastX = event.rawX.toInt(); lastY = event.rawY.toInt(); startX = p.x; startY = p.y }
            MotionEvent.ACTION_MOVE -> {
                p.x = startX + lastX - event.rawX.toInt()
                p.y = startY + event.rawY.toInt()
                panel?.let { wm.updateViewLayout(it, p) }
            }
        }
        return true
    }
    private var lastX = 0
    private var lastY = 0
    private var startX = 0
    private var startY = 0
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(CHANNEL, "Silent Pulse", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
    override fun onDestroy() {
        panel?.let { if (it.isAttachedToWindow) wm.removeView(it) }
        panel = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null
    companion object { private const val CHANNEL = "silent_pulse"; private const val NOTIFICATION_ID = 4107 }
}
