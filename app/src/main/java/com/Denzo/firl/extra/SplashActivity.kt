package com.Denzo.firl.extra;

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.Denzo.firl.R
import com.rbddevs.splashy.Splashy
import java.util.logging.Handler

class SplashActivity : AppCompatActivity() {

                         override fun onCreate(savedInstanceState: Bundle?) {
                             super.onCreate(savedInstanceState)
                             setContentView(R.layout.activity_main)
                             setSplashy()
                         }

                         private fun setSplashy() {
                             Splashy(this)
                                 .setLogo(R.drawable.ic_email_white_18dp)
                                 .setAnimation(Splashy.Animation.GROW_LOGO_FROM_CENTER)
                                 .setBackgroundResource(R.color.black)
                                 .setTitleColor(R.color.white)
                                 .setProgressColor(R.color.white)
                                 .setTitle(R.string.splashy)
                                 .setSubTitle(R.string.splash_screen_made_easy)
                                 .setFullScreen(true)
                                 .setSubTitleFontStyle("fonts/satisfy_regular.ttf")
                                 .setClickToHide(true)
                                 .setDuration(5000)
                                 .show()

                             Splashy.onComplete(object : Splashy.OnComplete {
                                 override fun onComplete() {
                                         Toast.makeText(this@SplashActivity, "Welcome", Toast.LENGTH_SHORT).show()
                                 }

                             })
                         }

                         public fun showSplash(v: View) {
                             setSplashy()
                             // Hides after 1sec
                             Handler().postDelayed({
                                 Splashy.hide()
                             }, 1000)
                         }

                     }