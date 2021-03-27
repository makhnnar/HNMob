package com.pedrogomez.hnmob.splash

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.pedrogomez.hnmob.R
import com.pedrogomez.hnmob.view.MainActivity

class SplashActivity : AppCompatActivity() {

    lateinit var counter : CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        counter = object : CountDownTimer(5000, 100){
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {
                goToMainActivity()
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        counter?.let{
            it.cancel()
        }
    }

    private fun goToMainActivity(){
        val intent = Intent(
            this@SplashActivity,
            MainActivity::class.java
        )
        startActivity(
            intent
        )
    }

}