package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    var timerBinder : TimerService.TimerBinder? = null

    var handler = Handler(Looper.getMainLooper()) {
        true
    }

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            timerBinder = (service as TimerService.TimerBinder)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            timerBinder = null
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )


        findViewById<Button>(R.id.startButton).setOnClickListener {
            timerBinder?.start(3000, handler)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            timerBinder?.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            timerBinder?.stop()
        }
    }
    override fun onDestroy() {
        unbindService(serviceConnection)
        super.onDestroy()
    }
}