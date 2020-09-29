package com.example.projectswitch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imageView.visibility = View.VISIBLE

        val animation = AnimationUtils.loadAnimation(this,R.anim.fade_in)
        imageView.startAnimation(animation)

//        imageView.animate().apply {
//            duration = 2000
//            rotationYBy(360f)
//        }.start()



        Handler().postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}