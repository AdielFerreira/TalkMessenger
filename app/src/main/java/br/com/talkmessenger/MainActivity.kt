package br.com.talkmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.talkmessenger.databinding.ActivityIntroBinding
import br.com.talkmessenger.ui.VerifyPhoneActivity


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.buttonLogin.setOnClickListener{
            Toast.makeText(this,"Teste",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,VerifyPhoneActivity::class.java))
        }
    }
}