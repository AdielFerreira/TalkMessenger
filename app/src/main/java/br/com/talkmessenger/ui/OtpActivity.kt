package br.com.talkmessenger.ui

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import br.com.talkmessenger.MainActivity
import br.com.talkmessenger.R
import br.com.talkmessenger.databinding.ActivityOtpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    private lateinit var phoneNumber:String
    private val binding by lazy { ActivityOtpBinding.inflate(layoutInflater) }

    //Firebase Auth Variables
    private lateinit var auth: FirebaseAuth
    private var storeVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var progressDialog: ProgressDialog
    private lateinit var mCounter:CountDownTimer
    //Firebase Auth Variables

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        startVerify()
    }

    private fun startVerify() {
        startPhoneNumberVerificaion(phoneNumber)
        startCounter(60000)
        progressDialog = createDialog("Enviando codigo de verificação",false)
        progressDialog.show()
    }

    private fun startCounter(time: Long) {
        binding.buttonResendSMS.isEnabled = false
        binding.txtViewCounter.isVisible = true


        mCounter = object : CountDownTimer(time,1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.txtViewCounter.text = "Tempo para envio: " + time/1000
            }

            override fun onFinish() {
                binding.buttonResendSMS.isEnabled = true
                binding.txtViewCounter.isVisible = false
            }
        }.start()
    }

    private fun init(){

        binding.buttonSendVerificationCode.setOnClickListener{
            val credential = PhoneAuthProvider.getCredential(storeVerificationId!!,binding.editTextVerifyPhone2SixDigitCode.text.toString())
            singInWithAuth(credential)
        }

        binding.buttonResendSMS.setOnClickListener{
            resendVerificationCode(phoneNumber,resendToken)
            startCounter(60000)
            progressDialog = createDialog("Enviando codigo de verificação novamente",false)
            progressDialog.show()
        }

        try {
            phoneNumber = intent.getStringExtra(PHONE_NUMBER)!!
            binding.txtViewVerifyPhone2Title.text = "Verificando $phoneNumber"
        }catch (e:Exception){
            Toast.makeText(this,"Numero nao encontrado, Tente novamente!",Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

        auth = Firebase.auth
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(verificationId, token)
                progressDialog.dismiss()

                storeVerificationId = verificationId
                resendToken = token
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                progressDialog.dismiss()

                val smsCode = credential.smsCode
                binding.editTextVerifyPhone2SixDigitCode.setText(smsCode)
                Log.i("Verificacao completa","A verificacao foi realizada com sucesso")
                singInWithAuth(credential)
            }


            override fun onVerificationFailed(e: FirebaseException) {
                progressDialog.dismiss()

                if(e is FirebaseAuthInvalidCredentialsException){
                    //Pedido invalido
                    Snackbar.make(findViewById(R.id.content),"Numero invalido!",Snackbar.LENGTH_SHORT).show()
                }else if(e is FirebaseTooManyRequestsException){
                    //Tempo excecido
                    Snackbar.make(findViewById(R.id.content),"Limite de envios SMS atingido!!",Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        startPhoneNumberVerificaion(phoneNumber)

    }

    private fun startPhoneNumberVerificaion(phoneNumber: String){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun singInWithAuth(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    if(task.result?.additionalUserInfo?.isNewUser == true){
                        showSingUpActivity()
                    }else{
                        showHomeActiviyt()
                    }
                }else{
                    progressDialog = createDialog("Verificação não concluida",false)
                    progressDialog.show()
                }

            }
    }

    private fun showSingUpActivity() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun showHomeActiviyt() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }


    //Extensions Functions
    fun Context.createDialog(message: String, isCancelable: Boolean): ProgressDialog{
        return ProgressDialog(this).apply{
            setCancelable(isCancelable)
            setMessage(message)
            setCanceledOnTouchOutside(false)
        }
    }

    private fun resendVerificationCode(
        phoneNumber: String
        ,token:PhoneAuthProvider.ForceResendingToken?){
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
        if(token != null){
            optionsBuilder.setForceResendingToken(token)
        }
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }
}