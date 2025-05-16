package com.example.urlreceiverwebview

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import java.net.ServerSocket

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private var running = true

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webView = WebView(this).apply {
            settings.javaScriptEnabled = true
        }
        setContentView(webView)

        Thread {
            try {
                val serverSocket = ServerSocket(11007)
                while (running) {
                    val socket = serverSocket.accept()
                    val url = socket.getInputStream().bufferedReader().readLine()
                    socket.close()
                    runOnUiThread { webView.loadUrl(url) }
                }
            } catch (e: Exception) {
                Log.e("ReceiverApp", "Error receiving data", e)
            }
        }.start()
    }

    override fun onDestroy() {
        running = false
        super.onDestroy()
    }
}