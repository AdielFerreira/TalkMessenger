package br.com.talkmessenger.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.format.Time
import br.com.talkmessenger.MainActivity
import br.com.talkmessenger.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.concurrent.DelayQueue

class SplashScreenActivity : AppCompatActivity() {

    val auth by lazy {
        Firebase.auth
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_splash_screen)

        if(auth.currentUser == null){
            Handler().postDelayed({startActivity(Intent(this,IntroActivity::class.java))},3000)

        }else{
            Handler().postDelayed({startActivity(Intent(this,MainActivity::class.java))},3000)

        }
    }

}