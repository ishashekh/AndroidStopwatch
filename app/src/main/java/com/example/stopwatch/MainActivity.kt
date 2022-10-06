package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    //variables
    lateinit var stopwatch: Chronometer     //the stopwatch
    var running = false                     //stopwatch running
    var offset: Long = 0                    //the base of the stopwatch

    // key strings for use with the bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get a reference to a stopwatch

        stopwatch = findViewById<Chronometer>(R.id.stopwatch)

        // restore the previous state (Bundle)
        if(savedInstanceState != null){
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running){
                stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if(!running){
                //set base time - Function
                setBaseTime()
                //start the stopwatch
                stopwatch.start()
                //set running = true
                running = true
            }
        }

        // the pause button pause the stopwatch if its running
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener{
            if(running){
                //save offsets <- reset back to 0
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }

        //reset set the offset and stopwatch to 0
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            // offset set to 0
            offset = 0
            //reset stopwatch to 0
            setBaseTime()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putLong(OFFSET_KEY,offset)
        savedInstanceState.putBoolean(RUNNING_KEY, running)
        savedInstanceState.putLong(BASE_KEY, stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        if(running){
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onPause(){
        super.onPause()
        if(running){
            saveOffset()
            stopwatch.stop()

        }
    }

    override fun onRestart(){
        super.onRestart()
        if(running){
            stopwatch.start()
            offset = 0
        }
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
    //update the stopwatch base time, allowing for any offset.
    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }
}