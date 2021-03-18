package com.lx.todaysbing

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = DrawableCrossFadeFactory.Builder()
            .setCrossFadeEnabled(true)
            .build()
        Glide.with(this)
            .load(R.drawable.microsoft_bing)
            .transition(withCrossFade(factory))
            .into(findViewById(R.id.image_view))

        countDownTimer = object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                navigate()
            }
        }
        countDownTimer?.start()
    }

    private fun navigate() {
        startActivity(Intent(this, BingActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        countDownTimer?.cancel()
        countDownTimer = null
    }
}