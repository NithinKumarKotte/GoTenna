package com.gotenna.android.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.gotenna.android.R

class SplashActivity : AppCompatActivity() {
    companion object{
        const val SPLASH_DELAY_TIMEOUT: Long = 2000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            try{
                startActivity(ListActivity.newIntent(this@SplashActivity))
                finish()
            }catch (e:Exception) {
                e.printStackTrace()
            }
        }, SPLASH_DELAY_TIMEOUT)
    }
}
