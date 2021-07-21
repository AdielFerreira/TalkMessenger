package br.com.talkmessenger.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import br.com.talkmessenger.R
import br.com.talkmessenger.databinding.ActivityVerifyPhoneBinding
import com.hbb20.CountryCodePicker


const val PHONE_NUMBER = "phone"
class VerifyPhoneActivity : AppCompatActivity() {


    private val phoneNumberEditText: EditText by lazy {
        findViewById<EditText>(R.id.edit_text_phoneNumber)
    }
    private val button: Button  by lazy {
        findViewById<Button>(R.id.button_next)
    }
    private lateinit var countryCode: String
    private lateinit var phoneNumber: String

    private val binding by lazy { ActivityVerifyPhoneBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        phoneNumberEditText.addTextChangedListener { value ->
            button.isEnabled = !(value.isNullOrEmpty() || value!!.length < 11)
        }

        button.setOnClickListener{
            chekcNumber()
        }

    }

    private fun chekcNumber() {
        countryCode = findViewById<CountryCodePicker>(R.id.ccp).selectedCountryCodeWithPlus
        phoneNumber = countryCode + phoneNumberEditText.text.toString()


        //"ADD VALIDATION OF PHONE NUMBER TO FIREBASE SERVER"


        startActivity(Intent(this, OtpActivity::class.java).putExtra(PHONE_NUMBER, phoneNumber))

    }
}