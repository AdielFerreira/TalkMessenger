package br.com.talkmessenger.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.talkmessenger.R
import br.com.talkmessenger.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.buttonLogin.setOnClickListener{
            startActivity(Intent(this,VerifyPhoneActivity::class.java))
        }
    }
}