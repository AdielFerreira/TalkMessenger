package br.com.talkmessenger.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.talkmessenger.databinding.ActivityVerifyPhoneBinding

class VerifyPhoneActivity : AppCompatActivity() {

    private val binding by lazy { ActivityVerifyPhoneBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}