package com.example.urlbroadcaster.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.net.Socket

class UrlSenderService : Service() {

    private lateinit var urls: List<String>
    private var index = 0
    private var running = true

    override fun onCreate() {
        super.onCreate()
        Toast.makeText(this, "UrlSenderService started", Toast.LENGTH_SHORT).show()

        urls = assets.open("urls.txt").bufferedReader().readLines()

        Thread {
            while (running) {
                sendUrl(urls[index])
                index = (index + 1) % urls.size
                Thread.sleep(10_000)
            }
        }.start()
    }

    private fun sendUrl(url: String) {
        try {
            Socket("127.0.0.1", 11007).use { socket ->
                socket.getOutputStream().write((url + "\n").toByteArray())
            }
        } catch (e: Exception) {
            Log.e("UrlSenderService", "Failed to send: $url", e)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        running = false
        Toast.makeText(this, "UrlSenderService stopped", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }
}